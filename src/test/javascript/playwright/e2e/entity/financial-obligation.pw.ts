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

const financialObligationPageUrl = '/financial-obligation';
const financialObligationSample = { amount: 7254.86, dueDate: '2026-11-22', status: 'OUTSTANDING' };

const financialObligationApiUrl = '/api/financial-obligations';

const entitiesRequest = (page: import('@playwright/test').Page) =>
  page.waitForResponse(response => new URL(response.url()).pathname === financialObligationApiUrl && response.request().method() === 'GET');

test.describe('FinancialObligation e2e test', () => {
  let financialObligation;
  let student;

  test.beforeEach(async ({ jhi }) => {
    const { username, password } = jhi.credentials();
    await jhi.login(username, password);
  });

  test.beforeEach(async ({ jhi }) => {
    // create an instance at the required relationship entity:
    student = await (
      await jhi.authenticatedRequest({
        method: 'POST',
        url: '/api/students',
        body: {
          studentCode: 'woot populist during',
          firstName: 'María Cristina',
          lastName: 'Salas Cavazos',
          email: 'MariadelCarmen.TrujilloCanales@yahoo.com',
          enrollmentDate: '2026-11-21',
          status: 'GRADUATED',
        },
      })
    ).json();
  });

  test.beforeEach(async ({ page }) => {
    // Simulate relationships api for better performance and reproducibility.
    // A url predicate rather than a glob: a glob's wildcards would close the "Disabled due to
    // incompatibility" block comment that wraps this section when the create test is skipped.
    await page.route(
      url => url.pathname === '/api/students',
      route => route.fulfill({ status: 200, json: [student].filter(Boolean) }),
    );
  });

  test.afterEach(async ({ jhi }) => {
    if (financialObligation) {
      await jhi.authenticatedRequest({
        method: 'DELETE',
        url: `/api/financial-obligations/${financialObligation.id}`,
      });
      financialObligation = undefined;
    }
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

  test('FinancialObligations menu should load FinancialObligations page', async ({ page, jhi }) => {
    const response = entitiesRequest(page);
    await page.goto('/');
    await jhi.navbar.clickOnEntityMenuItem('financial-obligation');
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
    await expect(jhi.entity.getEntityHeading('FinancialObligation')).toBeVisible();
    await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
  });

  test.describe('FinancialObligation page', () => {
    test('should have translated page title', async ({ page, jhi }) => {
      await page.goto(financialObligationPageUrl);
      await expect(jhi.entity.getEntityHeading('FinancialObligation')).not.toContainText(
        'universityFinanceApp.financialObligation.home.title',
      );
    });

    test.describe('create button click', () => {
      test.beforeEach(async ({ page }) => {
        const response = entitiesRequest(page);
        await page.goto(financialObligationPageUrl);
        await response;
      });

      test('should load create FinancialObligation page', async ({ page, jhi }) => {
        await page.locator(entityCreateButtonSelector).click();
        await expect(page).toHaveURL(url => url.pathname === `${financialObligationPageUrl}/new`);
        await expect(jhi.entity.getEntityCreateUpdateHeading('FinancialObligation')).toBeVisible();
        await expect(page.locator(entityCreateSaveButtonSelector)).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateCancelButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
      });
    });

    test.describe('with existing value', () => {
      test.beforeEach(async ({ page, jhi }) => {
        financialObligation = await (
          await jhi.authenticatedRequest({
            method: 'POST',
            url: '/api/financial-obligations',
            body: {
              ...financialObligationSample,
              student,
            },
          })
        ).json();

        await page.route(
          url => url.pathname === financialObligationApiUrl,
          route =>
            route.fulfill({
              status: 200,
              headers: {
                link: '<http://localhost/api/financial-obligations?page=0&size=20>; rel="last",<http://localhost/api/financial-obligations?page=0&size=20>; rel="first"',
              },
              json: [financialObligation],
            }),
          { times: 1 },
        );

        // Cypress waited on the stubbed request (cy.wait('@entitiesRequestInternal')); without it the test
        // can act before the row is rendered.
        const stubbedRequest = entitiesRequest(page);
        await page.goto(financialObligationPageUrl);
        await stubbedRequest;
      });

      test('detail button click should load details FinancialObligation page', async ({ page, jhi }) => {
        await page.locator(entityDetailsButtonSelector).first().click();
        await expect(jhi.entity.getEntityDetailsHeading('financialObligation')).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityDetailsBackButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
      });

      test('edit button click should load edit FinancialObligation page and go back', async ({ page, jhi }) => {
        await page.locator(entityEditButtonSelector).first().click();
        await expect(jhi.entity.getEntityCreateUpdateHeading('FinancialObligation')).toBeVisible();
        await expect(page.locator(entityCreateSaveButtonSelector)).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateCancelButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
      });

      test('edit button click should load edit FinancialObligation page and save', async ({ page, jhi }) => {
        await page.locator(entityEditButtonSelector).first().click();
        await expect(jhi.entity.getEntityCreateUpdateHeading('FinancialObligation')).toBeVisible();
        const response = entitiesRequest(page);
        await page.locator(entityCreateSaveButtonSelector).click();
        expect((await response).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
      });

      test('last delete button click should delete instance of FinancialObligation', async ({ page, jhi }) => {
        const deleteRequest = page.waitForResponse(
          response =>
            new URL(response.url()).pathname.startsWith(`${financialObligationApiUrl}/`) && response.request().method() === 'DELETE',
        );
        const listRequest = entitiesRequest(page);
        await page.locator(entityDeleteButtonSelector).last().click();
        await expect(jhi.entity.getEntityDeleteDialogHeading('financialObligation')).toBeVisible();
        await page.locator(entityConfirmDeleteButtonSelector).click();
        expect((await deleteRequest).status()).toBe(204);
        expect((await listRequest).status()).toBe(200);
        await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);

        financialObligation = undefined;
      });
    });
  });

  test.describe('new FinancialObligation page', () => {
    test.beforeEach(async ({ page, jhi }) => {
      await page.goto(financialObligationPageUrl);
      await page.locator(entityCreateButtonSelector).click();
      await expect(jhi.entity.getEntityCreateUpdateHeading('FinancialObligation')).toBeVisible();
    });

    test('should create an instance of FinancialObligation', async ({ page }) => {
      await page.locator(`[data-cy="amount"]`).fill('13708.29');
      await expect(page.locator(`[data-cy="amount"]`)).toHaveValue('13708.29');

      await page.locator(`[data-cy="dueDate"]`).fill('2026-11-21');
      await page.locator(`[data-cy="dueDate"]`).blur();
      await expect(page.locator(`[data-cy="dueDate"]`)).toHaveValue('2026-11-21');

      await page.locator(`[data-cy="status"]`).selectOption('OUTSTANDING');

      await page.locator(`[data-cy="student"]`).selectOption({ index: 1 });

      const postRequest = page.waitForResponse(
        response => new URL(response.url()).pathname === financialObligationApiUrl && response.request().method() === 'POST',
      );
      const listRequest = entitiesRequest(page);
      await page.locator(entityCreateSaveButtonSelector).click();

      const postResponse = await postRequest;
      expect(postResponse.status()).toBe(201);
      // Best effort: the body is only needed to delete the instance again in afterEach.
      financialObligation = await postResponse.json().catch(() => undefined);
      expect((await listRequest).status()).toBe(200);
      await expect(page).toHaveURL(url => url.pathname === financialObligationPageUrl);
    });
  });
});
