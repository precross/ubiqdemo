# ubiqdemo
# To Encrypt with eFPE
**Enpoint:**\
http://localhost:8080/api/v2/ubiq/encrypteFPE

**Request Type:** POST

**Request Body:**
{
    "plainText": "12-345-6789"
}

plainText min/max length should be 9 

**Response Body:**
00-00O-Bv8F

# To Decrypt with eFPE
**Enpoint:**\
http://localhost:8080/api/v2/ubiq/decrypteFpe

**Request Type:** GET\
It will return the decrypted data which was previously encrypted, so first you need to call encrypteFPE endpoint above before calling this endpoint.

**Response Body:**
12-345-6789

# To Encrypt Some Simple Data:
**Enpoint:**\
http://localhost:8080/api/v1/ubiq/encrypt

**Request Type:** POST

**Request Body:**
{
    "input": "This text will be encrypted"
}

**Response Body:**
{
    "respCode": "00",
    "respDescription": "Success",
    "output":"encrypted data"
}

# To Decrypt Data:
**Endpoint:**\
http://localhost:8080/api/v1/ubiq/decrypt

**Request Type:** GET\
It will decrypt the previously encrypted data. So you need to first call encrypt endpoint before calling this endpoint.

# To Encrypt Data From File:
**Endpoint:**\
http://localhost:8080/api/v1/ubiq/encryptFile

**Request Type:** POST

**Request Body:**
{
    "inputFilePath": "D:/Input/input.txt",
    "outputFilePath": "D:/Output/encrypted/output.enc"
}

**Response:**\
Encrypted Successfully

# To Decrypt Data From Encrypted File:
**Endpoint:**\
http://localhost:8080/api/v1/ubiq/decryptFile

**Request Type:** POST

**Request Body:**
{
    "inputFilePath": "D:/Output/encrypted/output.enc",
    "outputFilePath": "D:/Output/decrypted/output.txt"
}

**Response:**\
Decrypted Successfully
