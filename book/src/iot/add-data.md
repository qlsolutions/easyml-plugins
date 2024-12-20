# Add Data

## Step 1: Generate the Token Header

Before proceeding, ensure you have generated the token header (_SharedAccessSignature_) to authenticate your request.

---

## Step 2: API Endpoint

To add data to a specific series in a DataProvider, make a `POST` request to the following endpoint:

```http
{baseUrl}/api/v1/dataprovider/iotclient/serie/{serieId}
```

Replace `{baseUrl}` with the base URL of your EasyML server address and `{serieId}` with the unique identifier of the serie to which the data will be added.

---

## Step 3: Request Body

The body of the `POST` request should include the data points you want to add, structured as follows:

```json
{
  "data": [
    {
      "time": "string",
      "value": 0
    }
  ]
}
```

### Explanation of Fields
- **`time`**: The timestamp of the data point, in ISO 8601 format.
- **`value`**: The numeric value of the data point.

> **Note:** The `time` field must strictly follow the ISO 8601 format.

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
2. Make a `POST` request to `{baseUrl}/api/v1/dataprovider/iotclient/serie/{serieId}`.
3. Pass the data points in the request body, ensuring `time` is in ISO 8601 format.
4. Handle the response based on whether it's a success (HTTP 200) or an error.

This process allows you to effectively add data to a specific series in your DataProvider.