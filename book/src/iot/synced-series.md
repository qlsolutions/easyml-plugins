# Synced Series

## Step 1: Generate the Token Header

Before proceeding, ensure you have generated the token header (_SharedAccessSignature_) to authenticate your request.

---

## Step 2: API Endpoint

To retrieve the synced series, make a `GET` request to the following endpoint:

```http
{baseUrl}/api/v1/dataprovider/iotclient/syncedseries
```

Replace `{baseUrl}` with the base URL of your EasyML server address.

---

## Step 3: Understanding the Response

### Success Response
If the request is successful, the server will return an HTTP 200 response with the following structure:

```json
{
  "series": [
    {
      "data": [
        {
          "start": "string",
          "end": "string"
        }
      ],
      "id": "string"
    }
  ]
}
```

#### Explanation of Fields
- **`series`**: Array containing details of available series.
- **`id`**: Unique identifier of the series.
- **`start`** and **`end`**: Define the time range for the series in ISO 8601 format.
> Data outside this range will be **excluded**.

---

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

## Step 4: Use the Synced Series

Once the synced series are retrieved, you can use the `id`, `start`, and `end` fields to manage and send data for these series. Note that data outside the defined time range (`start` to `end`) will not be accepted.

For adding data to a series, refer to the [Add Data Guide](add-data.md).

---

## Summary

1. Generate a valid token header.
2. Make a `GET` request to `{baseUrl}/api/v1/dataprovider/iotclient/syncedseries`.
3. Parse the response to retrieve the `id` and time range (`start`, `end`) for each series.
4. Use the retrieved details to manage or add data to the series, ensuring data falls within the specified range.

This process ensures efficient synchronization and data management for your series in the DataProvider.