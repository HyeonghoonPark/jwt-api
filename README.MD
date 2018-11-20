# JWT RESTFUL API

--------------------------------------------------------
## Login
 
### 1. CheckMemberAuth

### 2. Create Token

    - Settings Token Info
        Token_Exp_Time (Millis)
        Token Key (Know Values Only Server)
        Token NickName (UserId)

    - Token Build (Use Jwt Builder Method)
        SetData
            setHeaderParam("Type", "")
                - ParameterType(String, String) 
                - TokenType
            setHeaderParam("regDate", "") 
                - ParameterType(String, String)
                - TokenRegTime
            setClaims() 
                - ParameterType(Map)
                - MapProperty : Key-NickName, Value-Id
            setExpiration()
                - ParameterType(Date)
                - TokenEndTime
            signWith()
            compact();
    - Throws Token in Cookie

--------------------------------------------------------

## AuthToken

### 1. Setting Interceptor

### 2. Check Token Info

--------------------------------------------------------