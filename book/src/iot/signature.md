# SharedAccessSignature Guide

## Step 1: Create an IoT DataProvider

First, create a **DataProvider** of type IoT. Once this is done, you will need to generate a _SharedAccessSignature_ to use the IoT API of the DataProviders. This signature will be passed in the `Authorization` header.

---

## Step 2: Define the SharedAccessSignature Structure

The _SharedAccessSignature_ consists of the following fields. Here's an illustrative example in Go:

```go
type TokenAccessType string

const (
	TokenAccessType_Read      TokenAccessType = "r"
	TokenAccessType_Write     TokenAccessType = "w"
	TokenAccessType_Delete    TokenAccessType = "d"
	TokenAccessType_ReadWrite TokenAccessType = "rw"
)

type SharedAccessSignature struct {
    Version        string
    CreatedAt      string // ISO 8601
    ExpireAt       string // ISO 8601
    Access         TokenAccessType
    DataProviderId string
}
```

### Key Field: `DataProviderId`
This field should contain the **ID** of the IoT DataProvider.

---

## Step 3: Parse the Signature Data

The signature is built from a string called _data_, which is created using the `Parse` function. This function concatenates the fields as follows:

```go
func (this *SharedAccessSignature) Parse() string {
    return fmt.Sprintf("sv=%s&st=%s&se=%s&sp=%v&sr=%s", this.Version, this.CreatedAt, this.ExpireAt, this.Access, this.DataProviderId)
}
```

---

## Step 4: Encrypt the Data

Encrypt the _data_ string using the HMAC-SHA256 algorithm. Use the `secretKey` parameter from the DataProvider as the key. Here's an example function to perform this encryption:

```go
// Function to encrypt a string using HMAC-SHA256
func EncryptHMACSHA256(message, secretKey string) string {
    // Create a new HMAC object using SHA256
    hmacSha256 := hmac.New(sha256.New, []byte(secretKey))

	// Write the message to the HMAC object
	hmacSha256.Write([]byte(message))

	// Compute the HMAC code
	encryptedMessage := hmacSha256.Sum(nil)

	// Convert the result to a hexadecimal string
	return hex.EncodeToString(encryptedMessage)
}
```

> **Note:** The resulting encrypted string is encoded in hexadecimal, not Base64.

---

## Step 5: Concatenate the Final Signature

The final _SharedAccessSignature_ is created by concatenating the components as shown below:

```go
"SharedAccessSignature " + data + "&sig=" + signature
```

---

## Summary

With the final _SharedAccessSignature_ string ready, you can now use it in the `Authorization` header to authenticate your requests to the IoT API of the DataProvider.