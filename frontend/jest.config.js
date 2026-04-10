module.exports = {
    preset: 'jest-puppeteer',
    testMatch: ['**/tests/e2e/**/*.test.js'],
    testTimeout: 30000,
    setupFilesAfterEnv: ['<rootDir>/tests/e2e/setup.js'],
    verbose: true
}