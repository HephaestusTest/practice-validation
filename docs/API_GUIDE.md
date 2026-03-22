# User API Usage Guide

This guide explains how to interact with the User API service, including authentication,
available endpoints, request/response formats, and error handling.

## Table of Contents

- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Endpoints](#endpoints)
  - [List Users](#list-users)
  - [Get User by ID](#get-user-by-id)
  - [Create User](#create-user)
- [Error Handling](#error-handling)
- [Rate Limiting](#rate-limiting)

## Getting Started

### Base URL

All API requests should be made to:

```
https://api.example.com/api
```

For local development:

```
http://localhost:8080/api
```

### Prerequisites

- Java 21 or later
- Maven 3.9+
- A running instance of the application server

## Authentication

The API currently supports basic token-based authentication. Include your API token
in the `Authorization` header:

```
Authorization: Bearer <your-token>
```

## Endpoints

### List Users

Retrieves all registered users.

**Request:**
```http
GET /api/users
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "email": "alice@example.com",
    "displayName": "Alice",
    "role": "USER"
  }
]
```

### Get User by ID

Retrieves a specific user by their unique identifier.

**Request:**
```http
GET /api/users/{id}
```

**Path Parameters:**

| Parameter | Type   | Description          |
|-----------|--------|----------------------|
| `id`      | `Long` | The unique user ID   |

**Response:** `200 OK`
```json
{
  "id": 1,
  "email": "alice@example.com",
  "displayName": "Alice",
  "role": "USER"
}
```

**Error Response:** `404 Not Found`
```json
{
  "error": "User not found: id=999"
}
```

### Create User

Creates a new user account and sends a welcome notification.

**Request:**
```http
POST /api/users
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "bob@example.com",
  "displayName": "Bob"
}
```

**Response:** `201 Created`
```json
{
  "id": 2,
  "email": "bob@example.com",
  "displayName": "Bob",
  "role": "USER"
}
```

**Error Response:** `409 Conflict`
```json
{
  "error": "Email already in use: bob@example.com"
}
```

## Error Handling

The API uses standard HTTP status codes:

| Code | Meaning               | Description                              |
|------|-----------------------|------------------------------------------|
| 200  | OK                    | Request succeeded                        |
| 201  | Created               | Resource created successfully            |
| 400  | Bad Request           | Invalid request body or parameters       |
| 404  | Not Found             | Requested resource does not exist        |
| 409  | Conflict              | Resource already exists (e.g., email)    |
| 500  | Internal Server Error | Unexpected server error                  |

All error responses follow this format:

```json
{
  "error": "Human-readable error message"
}
```

## Rate Limiting

The API enforces rate limiting to protect service stability:

- **Authenticated requests:** 1000 requests per hour per token
- **Unauthenticated requests:** 100 requests per hour per IP

When rate limited, the API returns `429 Too Many Requests` with a `Retry-After` header
indicating how many seconds to wait before retrying.
