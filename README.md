# Centralized Dependency Management and Release Automation Pipeline

## 1. Project Overview

This project is a full-stack web dashboard for visualizing a centralized dependency management and release automation system.

In a real DevOps setup, multiple services usually depend on shared libraries, common framework versions, Docker images, CI/CD pipelines, and release versions. Managing all of that manually can become difficult as the number of services grows.

This dashboard gives a single place to view:

- How many services are part of the platform
- Which service versions are currently deployed
- Which Docker image tags are running
- Which centralized dependencies are up to date or outdated
- What happened in recent pipeline runs
- Which releases have been published
- What release notes belong to each version

The current implementation uses mock data by default, so it works immediately without needing any external account or database. It also includes optional support for reading real GitHub Actions workflow runs.

## 2. Problem Statement

Modern software teams often manage many services at once. Each service may have:

- Its own dependency versions
- Its own Docker image tag
- Its own release version
- Its own pipeline status
- Its own deployment state

Without a centralized view, teams can lose track of:

- Which services are running outdated dependencies
- Whether the last pipeline succeeded or failed
- Which version was released most recently
- Which image tag is currently active
- Which release introduced a particular change

This project solves that by providing a simple, clean, professional dashboard for release and dependency visibility.

## 3. Main Goals

- Create a modern React dashboard for DevOps visibility
- Build a Node.js Express backend with REST APIs
- Use mock JSON data for easy local development
- Show service, dependency, pipeline, and release information
- Auto-refresh dashboard data every 10 seconds
- Keep the system easy to extend for real APIs later
- Provide optional GitHub Actions integration for real pipeline data

## 4. Tech Stack

### Frontend

- React 18
- Vite
- Tailwind CSS
- Lucide React icons
- JavaScript JSX components
- Fetch API for backend communication

### Backend

- Node.js
- Express.js
- CORS middleware
- Native `fetch` for optional GitHub Actions API calls
- Mock data module for local development

### DevOps / Tooling

- npm
- Vite development server
- Concurrently for running backend and frontend together
- GitHub Actions API integration support
- Environment variables for optional external integration

## 5. Current Features

### Overview Page

The dashboard shows high-level metrics:

- Total services
- Latest release version
- Last pipeline status
- Last run time
- Dependency health summary
- Pipeline success rate

### Services Panel

Shows all services in a table:

- Service name
- Runtime status
- Current deployed version
- Docker image tag
- Environment
- Uptime

Example services:

- `service-a`
- `service-b`
- `service-c`

### Dependency Management Panel

Shows centralized dependencies and whether updates are available.

Example dependencies:

- `express`
- `lodash`
- `react`
- `axios`

Outdated dependencies are highlighted with a warning style. Updated dependencies are shown with a healthy style.

### Pipeline Activity Panel

Shows the last 5 pipeline runs:

- Run ID
- Status
- Timestamp
- Commit message
- Branch
- Duration

Statuses include:

- `Success`
- `Failed`

### Release Panel

Shows release history:

- Release version
- Release date
- Release status
- Release notes

Example versions:

- `v1.2.0`
- `v1.1.0`
- `v1.0.0`

### Trigger Pipeline Button

The dashboard includes a mock `Trigger Pipeline` button.

When clicked, it sends a `POST` request to:

```text
/api/pipelines/trigger
```

The backend returns a fake queued pipeline response. This is useful for demonstrating how a real pipeline trigger could work later.

### Auto Refresh

The frontend refreshes dashboard data automatically every 10 seconds.

This logic lives in:

```text
src/hooks/useDashboardData.js
```

## 6. Architecture

The application has two main parts:

```text
React Frontend  --->  Express REST API  --->  Mock Data / Optional GitHub Actions API
```

### Frontend Responsibilities

- Render the dashboard UI
- Call backend APIs
- Display loading and error states
- Refresh data every 10 seconds
- Trigger mock pipeline runs
- Keep the UI responsive across screen sizes

### Backend Responsibilities

- Expose REST API endpoints
- Return mock dashboard data
- Build overview summary data
- Optionally fetch GitHub Actions workflow runs
- Handle mock pipeline trigger requests

## 7. Folder Structure

```text
.
|-- server/
|   |-- index.js
|   `-- mockData.js
|-- src/
|   |-- components/
|   |   |-- DependencyPanel.jsx
|   |   |-- MetricCard.jsx
|   |   |-- Panel.jsx
|   |   |-- PipelinePanel.jsx
|   |   |-- ReleasePanel.jsx
|   |   |-- ServicesPanel.jsx
|   |   `-- StatusBadge.jsx
|   |-- hooks/
|   |   `-- useDashboardData.js
|   |-- App.jsx
|   |-- main.jsx
|   `-- styles.css
|-- index.html
|-- package.json
|-- package-lock.json
|-- postcss.config.js
|-- tailwind.config.js
|-- vite.config.js
|-- .gitignore
`-- README.md
```

## 8. Important Files Explained

### `server/index.js`

Main Express server file.

It creates the REST API and defines all backend routes:

- `/api/overview`
- `/api/services`
- `/api/dependencies`
- `/api/pipelines`
- `/api/releases`
- `/api/pipelines/trigger`

It also includes optional GitHub Actions API logic.

### `server/mockData.js`

Contains all mock data used by the backend:

- Services
- Dependencies
- Pipelines
- Releases
- Overview summary builder

This is the best file to edit if you want to change sample data.

### `src/App.jsx`

Main React dashboard page.

It combines:

- Header
- Overview metric cards
- Services panel
- Dependency panel
- Pipeline panel
- Release panel
- Trigger pipeline action

### `src/hooks/useDashboardData.js`

Custom React hook that:

- Calls all backend APIs
- Stores API responses
- Handles loading state
- Handles error state
- Refreshes data every 10 seconds

### `src/components/`

Reusable dashboard components:

- `MetricCard.jsx`: Overview statistic card
- `Panel.jsx`: Shared panel layout
- `StatusBadge.jsx`: Status labels
- `ServicesPanel.jsx`: Services table
- `DependencyPanel.jsx`: Dependency health cards
- `PipelinePanel.jsx`: Pipeline activity table
- `ReleasePanel.jsx`: Release version list

### `tailwind.config.js`

Tailwind CSS theme configuration.

It defines custom colors and panel shadow styling.

### `vite.config.js`

Vite frontend configuration.

It also proxies frontend `/api` requests to the backend:

```js
proxy: {
  "/api": "http://localhost:5000"
}
```

## 9. REST API Details

### Health Check

```http
GET /api/health
```

Returns:

```json
{
  "status": "ok",
  "service": "release-dashboard-api"
}
```

### Overview

```http
GET /api/overview
```

Returns summary data:

```json
{
  "totalServices": 3,
  "latestReleaseVersion": "v1.2.0",
  "lastPipelineStatus": "Success",
  "lastRunTime": "2026-04-24T09:28:00.000Z",
  "dependencyHealth": {
    "total": 4,
    "outdated": 2
  },
  "pipelineSuccessRate": 80
}
```

### Services

```http
GET /api/services
```

Returns service inventory:

```json
[
  {
    "id": "service-a",
    "name": "service-a",
    "status": "Running",
    "currentVersion": "v1.2.0",
    "dockerImageTag": "registry.local/service-a:v1.2.0",
    "environment": "production",
    "uptime": "99.98%"
  }
]
```

### Dependencies

```http
GET /api/dependencies
```

Returns centralized dependency data:

```json
[
  {
    "name": "express",
    "currentVersion": "4.18.2",
    "latestVersion": "4.19.2",
    "owner": "platform-api",
    "usedBy": ["service-a", "service-b"],
    "outdated": true
  }
]
```

### Pipelines

```http
GET /api/pipelines
```

Returns recent pipeline runs:

```json
[
  {
    "id": 1056,
    "status": "Success",
    "timestamp": "2026-04-24T09:28:00.000Z",
    "commitMessage": "chore: publish centralized dependency bundle v1.2.0",
    "branch": "main",
    "duration": "3m 42s"
  }
]
```

### Trigger Pipeline

```http
POST /api/pipelines/trigger
```

Returns a mock queued pipeline:

```json
{
  "message": "Mock pipeline trigger accepted",
  "runId": 12345,
  "status": "Queued",
  "queuedAt": "2026-04-24T16:10:00.000Z"
}
```

### Releases

```http
GET /api/releases
```

Returns release history:

```json
[
  {
    "version": "v1.2.0",
    "date": "2026-04-24",
    "status": "Latest",
    "notes": [
      "Centralized dependency manifest promoted to production.",
      "Automated Docker image tagging added for all services.",
      "Release notes generated from pipeline metadata."
    ]
  }
]
```

## 10. How To Run From Zero

### Prerequisites

Install:

- Node.js 18 or newer
- npm

Check versions:

```bash
node --version
npm --version
```

### Install Dependencies

From the project root:

```bash
npm install
```

### Start Frontend And Backend Together

```bash
npm run dev
```

This starts:

- Express API on `http://localhost:5000`
- React dashboard on `http://localhost:5173`

Open:

```text
http://localhost:5173
```

### Start Backend Only

```bash
npm run server
```

Backend URL:

```text
http://localhost:5000
```

### Start Frontend Only

```bash
npm run client
```

Frontend URL:

```text
http://localhost:5173
```

### Build For Production

```bash
npm run build
```

The production build is generated in:

```text
dist/
```

### Preview Production Build

```bash
npm run preview
```

## 11. npm Scripts

```json
{
  "dev": "concurrently \"npm run server\" \"npm run client\"",
  "server": "node server/index.js",
  "client": "vite --host 0.0.0.0",
  "build": "vite build",
  "preview": "vite preview --host 0.0.0.0"
}
```

### Script Meaning

- `npm run dev`: Runs frontend and backend together
- `npm run server`: Runs only the Express backend
- `npm run client`: Runs only the Vite React frontend
- `npm run build`: Creates production frontend build
- `npm run preview`: Serves the production build locally

## 12. Optional GitHub Actions Integration

By default, pipeline data comes from mock data.

To use real GitHub Actions workflow runs, set these environment variables before starting the backend:

```bash
GITHUB_OWNER=your-github-owner
GITHUB_REPO=your-repository-name
GITHUB_TOKEN=your-github-token
```

### PowerShell Example

```powershell
$env:GITHUB_OWNER="your-github-owner"
$env:GITHUB_REPO="your-repository-name"
$env:GITHUB_TOKEN="your-github-token"
npm run dev
```

### What The Integration Does

When these variables are present, the backend calls:

```text
https://api.github.com/repos/{owner}/{repo}/actions/runs?per_page=5
```

Then it maps GitHub workflow run data into the dashboard pipeline format.

### Required GitHub Token Access

For public repositories, a fine-grained token with read-only access may be enough.

For private repositories, the token must be allowed to read Actions metadata for that repository.

## 13. Mock Data

Mock data is located in:

```text
server/mockData.js
```

You can edit this file to change:

- Service names
- Service statuses
- Versions
- Docker image tags
- Dependency versions
- Pipeline results
- Commit messages
- Release notes

Example dependency object:

```js
{
  name: "express",
  currentVersion: "4.18.2",
  latestVersion: "4.19.2",
  owner: "platform-api",
  usedBy: ["service-a", "service-b"],
  outdated: true
}
```

Example service object:

```js
{
  id: "service-a",
  name: "service-a",
  status: "Running",
  currentVersion: "v1.2.0",
  dockerImageTag: "registry.local/service-a:v1.2.0",
  environment: "production",
  uptime: "99.98%"
}
```

## 14. UI Design

The UI is designed to be:

- Modern
- Minimal
- Professional
- Responsive
- Easy to scan
- Useful for DevOps teams

The dashboard uses:

- Metric cards for quick status
- Tables for structured operational data
- Status badges for fast recognition
- Warning highlights for outdated dependencies
- Compact panels for readable grouping
- Responsive layout for laptop and desktop screens

## 15. Current Dashboard Sections

### Header

Shows the project name, last update time, refresh button, and trigger pipeline button.

### Overview Metrics

Shows four main metrics:

- Total Services
- Latest Release
- Last Pipeline
- Last Run Time

### Services

Shows service runtime and deployment metadata.

### Dependencies

Shows shared package versions and update status.

### Pipeline Activity

Shows recent build/release pipeline activity.

### Releases

Shows version history and release notes.

### Extra Summary Cards

Shows:

- Dependency health
- Image registry count
- Release source branch

## 16. How The Frontend Talks To The Backend

The frontend uses relative URLs like:

```js
fetch("/api/overview")
```

During development, Vite proxies `/api` to:

```text
http://localhost:5000
```

This avoids CORS issues during local development and keeps frontend code clean.

## 17. How Auto Refresh Works

The custom hook `useDashboardData`:

1. Calls all dashboard API endpoints
2. Stores the result in React state
3. Updates the `lastUpdated` timestamp
4. Repeats every 10 seconds using `setInterval`
5. Clears the interval when the component unmounts

## 18. How To Test APIs Manually

Open these URLs in the browser:

```text
http://localhost:5000/api/health
http://localhost:5000/api/overview
http://localhost:5000/api/services
http://localhost:5000/api/dependencies
http://localhost:5000/api/pipelines
http://localhost:5000/api/releases
```

Or use PowerShell:

```powershell
Invoke-RestMethod http://localhost:5000/api/overview
Invoke-RestMethod http://localhost:5000/api/services
```

## 19. Troubleshooting

### Port 5000 Is Already In Use

Set a different backend port:

```powershell
$env:PORT=5050
npm run server
```

If you change the backend port, also update the proxy target in:

```text
vite.config.js
```

### Port 5173 Is Already In Use

Vite may automatically choose another port. Check the terminal output for the actual frontend URL.

### API Data Is Not Loading

Make sure the backend is running:

```bash
npm run server
```

Then check:

```text
http://localhost:5000/api/health
```

### GitHub Actions Data Is Not Loading

Check:

- `GITHUB_OWNER` is correct
- `GITHUB_REPO` is correct
- `GITHUB_TOKEN` is valid
- The token has access to the repository
- The repository has GitHub Actions runs

### Dependencies Are Missing

Run:

```bash
npm install
```

### Production Build Fails

Run:

```bash
npm run build
```

Then read the terminal error. Most build errors will point to the exact file and line number.

## 20. Future Improvements

Possible next steps:

- Add authentication
- Add real database storage
- Add real dependency scanning from package files
- Add Docker registry integration
- Add Kubernetes service health checks
- Add real pipeline trigger using GitHub Actions workflow dispatch
- Add charts for pipeline success rate over time
- Add service detail pages
- Add filters for environment, service, branch, and status
- Add dark mode
- Add deployment approval workflow
- Add release comparison view
- Add Slack or Teams notification integration

## 21. Project Summary

This project demonstrates a practical DevOps dashboard for centralized dependency management and release automation.

It is intentionally simple, clean, and extensible:

- React handles the user interface
- Tailwind CSS handles styling
- Express exposes REST APIs
- Mock data makes the project immediately runnable
- Optional GitHub Actions integration shows how real pipeline data can be added

The result is a professional dashboard that gives engineering teams quick visibility into service status, dependency health, pipeline activity, and release history.
