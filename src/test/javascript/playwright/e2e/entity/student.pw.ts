import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';
import { expect, test } from '../../support/fixtures';

const studentPageUrl = '/student';
const studentSample = {
  studentCode: 'whenever tangible',
  firstName: 'Olivia',
  lastName: 'Caldera Velásquez',
  email: 'AnaLuisa.PerezTerrazas17@yahoo.com',
  enrollmentDate: '2026-11-21',
  status: 'GRADUATED',
};

const studentApiUrl = '/api/students';

const entitiesRequest = (page: import('@playwright/test').Page) =>
  page.waitForResponse(response => new URL(response.url()).pathname === studentApiUrl && response.request().method() === 'GET');

test.describe('Student e2e test', () => {
  let student;

  test.beforeEach(async ({ jhi }) => {
    const { username, password } = jhi.credentials();
    await jhi.login(username, password);
  });

  test.afterEach(async ({ jhi }) => {
    if (student) {
      await jhi.authenticatedRequest({
        method: 'DELETE',
        url: `/api/students/${student.id}`,
      });
      student = undefined;
    }
  });

  test('Students menu should load Students page', async ({ page, jhi }) => {
    const response = entitiesRequest(page);
    await page.goto('/');
    await jhi.navbar.clickOnEntityMenuItem('student');
    // Chrome drops the response body once the page navigates, unlike the cypress proxy, so the table
    // assertion is only made when the payload is still readable.
    const body = (await (await response).json().catch(() => undefined)) as unknown[] | undefined;
    if (Array.isArray(body)) {
      if (body.length === 0) {
        await expect(page.locator(entityTableSelector)).toHaveCount(0);
      } else {
        // The clients render one entityTable element per row, so match the first one.
        await expect(page.locator(entityTableSelector).first()).toBeVisible();
      }
    }
    await expect(jhi.entity.getEntityHeading('Student')).toBeVisible();
    await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
  });

  test.describe('Student page', () => {
    test('should have translated page title', async ({ page, jhi }) => {
      await page.goto(studentPageUrl);
      await expect(jhi.entity.getEntityHeading('Student')).not.toContainText('universityFinanceApp.student.home.title');
    });

    test.describe('create button click', () => {
      test.beforeEach(async ({ page }) => {
        const response = entitiesRequest(page);
        await page.goto(studentPageUrl);
        await response;
      });

      test('should load create Student page', async ({ page, jhi }) => {
        await page.locator(entityCreateButtonSelector).click();
        await expect(page).toHaveURL(url => url.pathname === `${studentPageUrl}/new`);
        await expect(jhi.entity.getEntityCreateUpdateHeading('Student')).toBeVisible();
        await expect(page.locator(entityCreateSaveButtonSelector)).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateCancelButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
      });
    });

    test.describe('with existing value', () => {
      test.beforeEach(async ({ page, jhi }) => {
        student = await (
          await jhi.authenticatedRequest({
            method: 'POST',
            url: '/api/students',
            body: studentSample,
          })
        ).json();

        await page.route(
          url => url.pathname === studentApiUrl,
          route =>
            route.fulfill({
              status: 200,
              headers: {
                link: '<http://localhost/api/students?page=0&size=20>; rel="last",<http://localhost/api/students?page=0&size=20>; rel="first"',
              },
              json: [student],
            }),
          { times: 1 },
        );

        // Cypress waited on the stubbed request (cy.wait('@entitiesRequestInternal')); without it the test
        // can act before the row is rendered.
        const stubbedRequest = entitiesRequest(page);
        await page.goto(studentPageUrl);
        await stubbedRequest;
      });

      test('detail button click should load details Student page', async ({ page, jhi }) => {
        await page.locator(entityDetailsButtonSelector).first().click();
        await expect(jhi.entity.getEntityDetailsHeading('student')).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityDetailsBackButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
      });

      test('edit button click should load edit Student page and go back', async ({ page, jhi }) => {
        await page.locator(entityEditButtonSelector).first().click();
        await expect(jhi.entity.getEntityCreateUpdateHeading('Student')).toBeVisible();
        await expect(page.locator(entityCreateSaveButtonSelector)).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateCancelButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
      });

      test('edit button click should load edit Student page and save', async ({ page, jhi }) => {
        await page.locator(entityEditButtonSelector).first().click();
        await expect(jhi.entity.getEntityCreateUpdateHeading('Student')).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateSaveButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
      });

      test('last delete button click should delete instance of Student', async ({ page, jhi }) => {
        const deleteRequest = page.waitForResponse(
          response => new URL(response.url()).pathname.startsWith(`${studentApiUrl}/`) && response.request().method() === 'DELETE',
        );
        const listRequest = entitiesRequest(page);
        await page.locator(entityDeleteButtonSelector).last().click();
        await expect(jhi.entity.getEntityDeleteDialogHeading('student')).toBeVisible();
        await page.locator(entityConfirmDeleteButtonSelector).click();
        expect((await deleteRequest).status()).toBe(204);
        expect((await listRequest).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === studentPageUrl);

        student = undefined;
      });
    });
  });

  test.describe('new Student page', () => {
    test.beforeEach(async ({ page, jhi }) => {
      await page.goto(studentPageUrl);
      await page.locator(entityCreateButtonSelector).click();
      await expect(jhi.entity.getEntityCreateUpdateHeading('Student')).toBeVisible();
    });

    test('should create an instance of Student', async ({ page }) => {
      await page.locator(`[data-cy="studentCode"]`).fill('oof consequently wha');
      await expect(page.locator(`[data-cy="studentCode"]`)).toHaveValue('oof consequently wha');

      await page.locator(`[data-cy="firstName"]`).fill('Lucas');
      await expect(page.locator(`[data-cy="firstName"]`)).toHaveValue('Lucas');

      await page.locator(`[data-cy="lastName"]`).fill('Mireles Amador');
      await expect(page.locator(`[data-cy="lastName"]`)).toHaveValue('Mireles Amador');

      await page.locator(`[data-cy="email"]`).fill('Marilu_AmadorDavila60@gmail.com');
      await expect(page.locator(`[data-cy="email"]`)).toHaveValue('Marilu_AmadorDavila60@gmail.com');

      await page.locator(`[data-cy="enrollmentDate"]`).fill('2026-11-21');
      await page.locator(`[data-cy="enrollmentDate"]`).blur();
      await expect(page.locator(`[data-cy="enrollmentDate"]`)).toHaveValue('2026-11-21');

      await page.locator(`[data-cy="status"]`).selectOption('GRADUATED');

      const postRequest = page.waitForResponse(
        response => new URL(response.url()).pathname === studentApiUrl && response.request().method() === 'POST',
      );
      const listRequest = entitiesRequest(page);
      await page.locator(entityCreateSaveButtonSelector).click();

      const postResponse = await postRequest;
      expect(postResponse.status()).toBe(201);
      // Best effort: the body is only needed to delete the instance again in afterEach.
      student = await postResponse.json().catch(() => undefined);
      expect((await listRequest).status()).toBe(200);
      await expect(page).toHaveURL(url => url.pathname === studentPageUrl);
    });
  });
});
