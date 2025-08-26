# 🔍 **Complete Found Controller API Testing Guide**

## 📋 **All Found Controller Endpoints with Test Cases**

### **🔐 Prerequisites - Setup First**

#### **1. Register Two Users (Owner & Finder)**
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "name": "Item Owner",
    "email": "owner@kiit.ac.in",
    "password": "owner123"
}
```

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
    "name": "Item Finder", 
    "email": "finder@kiit.ac.in",
    "password": "finder123"
}
```

#### **2. Login & Get Tokens**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "owner@kiit.ac.in",
    "password": "owner123"
}
```
**📝 Save OWNER_TOKEN from response**

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
    "email": "finder@kiit.ac.in", 
    "password": "finder123"
}
```
**📝 Save FINDER_TOKEN from response**

#### **3. Create Test Item (as Owner)**
```http
POST http://localhost:8080/api/items
Authorization: Bearer OWNER_TOKEN
Content-Type: application/json

{
    "title": "Lost iPhone 15 Pro",
    "description": "Black iPhone 15 Pro with cracked screen protector",
    "location": "Central Library",
    "category": "Electronics", 
    "status": "LOST",
    "reward": 5000.0,
    "imageUrl": "https://example.com/iphone.jpg"
}
```
**📝 Save ITEM_ID from response**

---

## 🎯 **Found Controller API Tests**

### **1. Mark Item as Found**
```http
POST http://localhost:8080/api/found/mark
Authorization: Bearer FINDER_TOKEN
Content-Type: application/json

{
    "itemId": ITEM_ID,
    "message": "Found this iPhone near CS Department entrance. Has the same cracked screen protector!"
}
```

**Expected Response (200 OK):**
```json
{
    "id": 1,
    "itemId": ITEM_ID,
    "itemTitle": "Lost iPhone 15 Pro",
    "finderName": "Item Finder",
    "ownerName": "Item Owner", 
    "finderId": 2,
    "ownerId": 1,
    "finderConfirmed": true,
    "ownerConfirmed": false,
    "finderConfirmedAt": "2025-08-25T23:45:00.123456789",
    "ownerConfirmedAt": null,
    "createdAt": "2025-08-25T23:45:00.123456789",
    "finderMessage": "Found this iPhone near CS Department entrance...",
    "bothConfirmed": false
}
```

---

### **2. Get Pending Confirmations (Owner's View)**
```http
GET http://localhost:8080/api/found/pending-confirmation
Authorization: Bearer OWNER_TOKEN
```

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "itemId": ITEM_ID,
        "itemTitle": "Lost iPhone 15 Pro",
        "finderName": "Item Finder",
        "ownerName": "Item Owner",
        "finderId": 2,
        "ownerId": 1, 
        "finderConfirmed": true,
        "ownerConfirmed": false,
        "finderConfirmedAt": "2025-08-25T23:45:00.123456789",
        "ownerConfirmedAt": null,
        "createdAt": "2025-08-25T23:45:00.123456789",
        "finderMessage": "Found this iPhone near CS Department entrance...",
        "bothConfirmed": false
    }
]
```

---

### **3. Owner Confirms Found**
```http
POST http://localhost:8080/api/found/confirm/1
Authorization: Bearer OWNER_TOKEN
```

**Expected Response (200 OK):**
```json
{
    "message": "Item confirmed as found and removed from listings!",
    "bothConfirmed": "true"
}
```

---

### **4. Get My Found Items (Finder's View)**
```http
GET http://localhost:8080/api/found/my-found-items
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "itemId": ITEM_ID,
        "itemTitle": "Lost iPhone 15 Pro", 
        "finderName": "Item Finder",
        "ownerName": "Item Owner",
        "finderId": 2,
        "ownerId": 1,
        "finderConfirmed": true,
        "ownerConfirmed": true,
        "finderConfirmedAt": "2025-08-25T23:45:00.123456789",
        "ownerConfirmedAt": "2025-08-25T23:46:00.987654321",
        "createdAt": "2025-08-25T23:45:00.123456789",
        "finderMessage": "Found this iPhone near CS Department entrance...",
        "bothConfirmed": true
    }
]
```

---

### **5. Check If Item Has Found Markings**
```http
GET http://localhost:8080/api/found/check/ITEM_ID
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (200 OK):**
```json
{
    "hasFoundMarkings": true
}
```

---

### **6. Get Found Records for Specific Item**
```http
GET http://localhost:8080/api/found/item/ITEM_ID
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (200 OK):**
```json
[
    {
        "id": 1,
        "itemId": ITEM_ID,
        "itemTitle": "Lost iPhone 15 Pro",
        "finderName": "Item Finder", 
        "ownerName": "Item Owner",
        "finderId": 2,
        "ownerId": 1,
        "finderConfirmed": true,
        "ownerConfirmed": true,
        "finderConfirmedAt": "2025-08-25T23:45:00.123456789",
        "ownerConfirmedAt": "2025-08-25T23:46:00.987654321",
        "createdAt": "2025-08-25T23:45:00.123456789",
        "finderMessage": "Found this iPhone near CS Department entrance...",
        "bothConfirmed": true
    }
]
```

---

### **7. Cancel Found Marking (Before Owner Confirms)**
**⚠️ Note: Only test this before owner confirmation**

```http
DELETE http://localhost:8080/api/found/cancel/1
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (200 OK):**
```json
{
    "message": "Found marking cancelled successfully"
}
```

---

## ❌ **Error Case Testing**

### **8. Try to Mark Own Item as Found**
```http
POST http://localhost:8080/api/found/mark
Authorization: Bearer OWNER_TOKEN
Content-Type: application/json

{
    "itemId": ITEM_ID,
    "message": "Testing own item marking"
}
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "You cannot mark your item as found"
}
```

---

### **9. Try to Mark Same Item Twice**
```http
POST http://localhost:8080/api/found/mark
Authorization: Bearer FINDER_TOKEN
Content-Type: application/json

{
    "itemId": ITEM_ID,
    "message": "Trying to mark again"
}
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "You have already marked this item as found"
}
```

---

### **10. Try to Confirm Without Permission**
```http
POST http://localhost:8080/api/found/confirm/1
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "You can only confirm your own item"
}
```

---

### **11. Try to Cancel After Owner Confirms**
```http
DELETE http://localhost:8080/api/found/cancel/1
Authorization: Bearer FINDER_TOKEN
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "Cannot cancel after owner has confirmed"
}
```

---

### **12. Try to Cancel Someone Else's Found Marking**
```http
DELETE http://localhost:8080/api/found/cancel/1
Authorization: Bearer OWNER_TOKEN
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "You can only cancel your own found markings"
}
```

---

### **13. Invalid Item ID**
```http
POST http://localhost:8080/api/found/mark
Authorization: Bearer FINDER_TOKEN
Content-Type: application/json

{
    "itemId": 99999,
    "message": "Testing invalid item"
}
```

**Expected Response (400 Bad Request):**
```json
{
    "error": "Item not Found"
}
```

---

### **14. Missing Authorization Token**
```http
POST http://localhost:8080/api/found/mark
Content-Type: application/json

{
    "itemId": ITEM_ID,
    "message": "Testing without token"
}
```

**Expected Response (401 Unauthorized)**

---

### **15. Invalid Token**
```http
GET http://localhost:8080/api/found/my-found-items
Authorization: Bearer INVALID_TOKEN
```

**Expected Response (401 Unauthorized)**

---

## � **Common Issues & Solutions**

### **Issue 1: Hibernate TransientObjectException**
**Error:** `persistent instance references an unsaved transient instance`

**Solution:** ✅ **FIXED** - Updated FoundService to properly handle entity relationships by explicitly setting all properties instead of using constructor.

### **Issue 2: NullPointerException on reportedBy**
**Error:** `Cannot invoke User.getId() because reportedBy is null`

**Solution:** ✅ **FIXED** - Added fallback to use `postedBy` when `reportedBy` is null.

### **Issue 3: Wrong Found ID for Confirmation**
**Error:** `Found record not found`

**Solution:** Use the correct **Found ID** from pending confirmations response, not the Item ID.
- Item ID = 20 (the lost item)
- Found ID = 4 (the found record) ← Use this for confirmation

---

## �🔄 **Complete Test Workflow**

### **Sequential Testing Order:**
1. ✅ Setup (Register → Login → Create Item)
2. ✅ Mark as Found (#1)
3. ✅ Check Pending Confirmations (#2)  
4. ✅ Check Found Status (#5)
5. ✅ Get Found Records (#6)
6. ✅ Owner Confirms (#3)
7. ✅ Check My Found Items (#4)
8. ✅ Test Error Cases (#8-15)

### **Status Flow Verification:**
```
LOST → (mark found) → FOUND_PENDING → (owner confirms) → RESOLVED (item no longer appears in active listings)
```

## 📊 **Postman Collection Variables**

Set these in Postman Environment:
- `baseUrl`: `http://localhost:8080/api`
- `ownerToken`: `OWNER_JWT_TOKEN`
- `finderToken`: `FINDER_JWT_TOKEN`
- `itemId`: `ACTUAL_ITEM_ID`

Then use: `{{baseUrl}}/found/mark` and `Bearer {{finderToken}}`

## 🎯 **Expected HTTP Status Codes**

| Endpoint | Success | Error Cases |
|----------|---------|-------------|
| POST /mark | 200 OK | 400 (business logic), 401 (auth) |
| POST /confirm/{id} | 200 OK | 400 (permission), 401 (auth) |
| GET /pending-confirmation | 200 OK | 401 (auth) |
| GET /my-found-items | 200 OK | 401 (auth) |
| GET /check/{itemId} | 200 OK | 401 (auth) |
| GET /item/{itemId} | 200 OK | 401 (auth) |
| DELETE /cancel/{id} | 200 OK | 400 (permission), 401 (auth) |

**🎉 This covers all Found Controller endpoints comprehensively!**
