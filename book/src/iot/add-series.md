# Add Series

## Step 1: Generate the Token Header

Before proceeding, ensure you have generated the token header (_SharedAccessSignature_) to authenticate your request.

---

## Step 2: API Endpoint

To add series to a DataProvider, make a `POST` request to the following endpoint:

```http
{baseUrl}/api/v1/dataprovider/iotclient/series
```

Replace `{baseUrl}` with the base URL of your EasyML server address.

---

## Step 3: Request Body

The body of the `POST` request should include the series you want to add, structured as follows:

```json
{
  "series": [
    {
       "id": "string",
       "diplayName": "string",
        "tags": [
          "string"
        ]
    }
  ]
}
```

### Explanation of Fields
- **`id`**: Unique identifier for the series.
- **`displayName`**: Human-readable name for the series.
- **`tags`**: Array of tags associated with the series.

---

## Step 4: Response Handling

### Success Response
If the request is successful, the server will return an HTTP 200 response.

### Error Response
If there is an issue, you will receive a response detailing the cause of the failure. The response will look like this:

```json
{
  "cause": "string",
  "details": {},
  "error": "string"
}
```

#### Error Fields
- **`cause`**: Brief description of the issue.
- **`details`**: Additional details about the error (may vary depending on the implementation).
- **`error`**: Specific error message.

---

## Summary

1. Generate a valid token header.
2. Make a `POST` request to `{baseUrl}/api/v1/dataprovider/iotclient/series`.
3. Pass the series data in the request body.
4. Handle the response based on whether it's a success (HTTP 200) or an error.

This process allows you to add series to a DataProvider effectively.