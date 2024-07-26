# SSO Steps (user not Authorized)

##User's Device (Browser):
***The user's browser initiates an access request to the Front-End application.***
##Front-End Application:
***Receives the access request and sends an authentication request to the Back-End server.***

##Back-End Server:
***Handles the authentication request by validating the user's credentials with the Authentication Server (AD/LDAP).
Upon successful validation, generates an authentication token.***

##Authentication Server (AD/LDAP):
***Validates the user's credentials received from the Back-End server.***

##Token Storage:
***The generated token is sent back to the Front-End and stored securely (e.g., in cookies or local storage).***

---

# SSO Steps (user Authorized)

##Browser Request:
***The user’s browser sends a request to access the Front-End application.***

##Check for Existing Token:
***The Front-End checks if there is an existing authentication token (e.g., in cookies or local storage).***

##Validate Token:
***If a token is found, the Front-End sends it to the Back-End server to validate the token.***

##Token Validation:
***The Back-End server verifies the token’s validity (e.g., checking its signature, expiration, and associated claims).***

##Access Granted:
***If the token is valid, the Back-End grants access to the requested resources.
If the token is invalid or expired, the Back-End redirects the user to re-authenticate.***

##Token Renewal (if needed):
***If the token is near expiration, the Back-End may issue a new token and send it to the Front-End to update the stored token.***
