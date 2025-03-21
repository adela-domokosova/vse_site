# VŠE page



Webová stránka s omezeným množstvím pixelů, které si lidé mohou koupit za symbolickou cenu 1 pixel za 1 korunu. Koupené pixely je možné zabarvit a vytvářet obrázky nebo slova, která se budou zobrazovat na domovské stránce. Stránka je zaměřena na studenty VŠE, ale nebrání se ani propagaci firem. Pixely si ale každý kupuje pouze na omezenou dobu. Po skončení období, jsou vybrané peníze odevzdány studentům z fakulty, která měla nejvíce pixelů. Aktuální skóre je možné sledovat na leaderboardu.

## Funkcionalita

Lidé si mohou vytvořit uživatelský profil, který je spojen a ověřen se školní emailovou adresou a fakultou VŠE.

Registrovaní uživatelé mohou kopit množství pixelů a libovolně je zbarvit, ale pouze na omezenou dobu.

Všichni uživatelé mohou sledovat scoreboard, který zobrazuje, která fakulta aktuálně nakoupila nejvíce pixelů v daném období. Dále mohou sledovat obrázek na homepage.

Moderátoři mají možnost mazat obsah nesplňující stanovené podmínky.

- návrhu úložiště
  - MySQL databáze na AWS, databáze se generuje podle dat v Entity balíčku, obsahuje tabulku uživatelů, pixelů a transakcí. S databází komunikujeme pomocí Repositories.



### Diagram případů užití
```plantuml
@startuml
actor Registered as u
actor Notregistered as n 
actor Admin as a
usecase "view home page and scoreboard" as UC1
usecase "log in" as UC2
usecase "create account" as UC3
usecase "select pixels with payment" as UC4
usecase "edit pixels on homepage" as UC5
usecase "ban user" as UC6
usecase "logout" as UC7
a --> UC5
u --> UC4
n --> UC3
u --> UC2
a --> UC2
a --> UC1
u --> UC1
n --> UC1
a --> UC6
u --> UC7
@enduml
```

```plantuml
@startuml
class User {
  +userID: int
  +name: String
  +email: String
  +role: String
  +faculty: String
  +buyPixel()
  +colorPixel()
  +updateProfile()
}

class Canvas {
  +canvasID: int
  +width: int
  +height: int
  +pixels: List<Pixel>
  +addPixel()
  +editPixels()
  +removePixel()
}

class Pixel {
  +pixelID: int
  +xPosition: int
  +yPosition: int
  +color: String
  +owner: User
  +changeColor()
}

class HomePage {
  +images: List<Image>
  +texts: List<Text>
  +displayContent()
}

class Admin {
  +adminID: int
  +name: String
  +email: String
  +editCanvas()
  +banUser()
}

class Scoreboard {
  +scores: Map<Faculty, int>
  +updateScore()
  +displayScores()
}

class Login {
  +loginID: int
  +username: String
  +password: String
  +authenticate()
}

class Registration {
  +registrationID: int
  +username: String
  +email: String
  +password: String
  +faculty: String
  +registerUser()
}

class Profile {
  +profileID: int
  +user: User
  +editProfile()
}


User --> Canvas : buys pixels
Canvas --> Pixel : contains
Pixel --> HomePage : displays on
Admin --> Canvas : edits pixels on canvas
User --> Login : logs in
User --> Registration : registers
User --> Scoreboard : updates score
User --> Profile : edits profile
Admin --> Scoreboard : view score
Admin --> Login : logs in
Admin --> Profile : view profile
Admin --> User : bans user
@enduml
```

```plantuml
@startuml
entity users {
    * id : Long
    * username : String
    * email : String
    * password : String
    * role : String
    * faculty : String
    * password_tobe : String
}

entity grid_cells {
    * id : Long
    * gridRow : int
    * col : int
    * color : String
    * user : Long
    * faculty : String
}

entity transactions {
    * id : Long
    * amount : int
    * userId : Long
    * timestamp : String
    * cardNum : Long
    * purchasedPixels : String
}

users ||--o{ grid_cells : owns
users ||--o{ transactions : makes
@enduml
```


```plantuml
@startuml
start

:Registered User;
:Login to Account;
:Navigate to Pixel Purchase Page;
:Select Pixels to Buy;
:Review Selection;
if (Accept Selection?) then (Yes)
    :Proceed to Payment;
    :Complete Payment Process;
    :View Purchased Pixels on Homepage;
    :View purchased pixels in Scoreboard;
else (No)
    
endif

stop
@enduml
```
```plantuml
@startuml
start
:Login as Admin;
:Click on the Edit Pixel tab;
if (Ban/Unban User?) then (yes)
  :Ban/Unban User;
  :Confirm Ban/Unban;
endif
:Select the Pixels to Remove;
:Save Changes;
:View the Updated Grid on the Homepage;
stop
@enduml
```


```plantuml
@startuml
actor "Not Registered" as NotRegistered
actor "Registered User" as Registered
actor Admin

participant HomePage
participant Scoreboard
participant BuyPixels
participant PaymentGateway
participant About


' Not Registered User Interaction
NotRegistered -> HomePage : "View homepage and scoreboard"
NotRegistered -> Scoreboard : "View scoreboard"
NotRegistered -> About : "View about"
NotRegistered -> HomePage : "Sign Up"

' Registered User Interaction
Registered -> HomePage : "Login"
Registered -> HomePage : "Logout"
Registered -> PixelEditor : "Navigate to pixel selection"
PixelEditor -> Registered : "Display pixel selection page"
Registered -> PixelEditor : "Select pixels to purchase"
PixelEditor -> Registered : "Review selected pixels"
Registered -> PaymentGateway : "Proceed to payment"
PaymentGateway -> Registered : "Complete payment"
PaymentGateway -> Scoreboard : "Update scoreboard"
Scoreboard -> Registered : "View updated scores"

' Admin Interaction
Admin -> PixelEditor : "Edit pixels on homepage"
Admin -> Registered : "Ban user"
@enduml
```


```plantuml
@startuml
class AdminController {
    +handleAdminHome(): String
    +handleDeletePixels(): String
    +banUser(payload: Map<String, String>): ResponseEntity<String>
    +handleSaveDelete(changedCells: Map<String, String>): ResponseEntity<Map<String, String>>
}

class MyUserDetailService {
    +loadUserByUsername(username: String): UserDetails
    +getUserByUsername(username: String): Optional<MyUser>
}

class UserServices {
    +register(user: MyUser, siteURL: String)
    +changeToBePassword(user: MyUser, siteURL: String)
    +verify(verificationCode: String): boolean
    +verifyChangePassword(verificationCode: String): boolean
    +banUser(user: MyUser, num: int): Boolean
    +changePassword(user: MyUser, currentPassword: String, newPassword: String): boolean
}


class MyUserRepository {
    +findByUsername(username: String): Optional<MyUser>
    +findByEmail(email: String): Optional<MyUser>
    +findByVerificationCode(code: String): MyUser
}

class GridCellRepository {
    +findByGridRowAndCol(row: int, col: int): GridCell
}
class GridCellServices {
    +gridCellRepository: GridCellRepository
    +transactionRepository: TransactionRepository
    +myUserRepository: MyUserRepository
    +getAllGridCells(): List<GridCell>
    +saveGridCell(gridCell: GridCell): void
    +getGridColors(): Map<String, String>
    +getGridColorsandUsers(): Map<String, String>
    +getFacultyCounts(): Map<String, Long>
    +getColorFromDatabase(row: int, col: int): String
    +getColorandUserFromDatabase(row: int, col: int): String
}

AdminController --> UserServices
AdminController --> MyUserDetailService
AdminController --> GridCellServices

MyUserDetailService --> MyUserRepository
UserServices --> MyUserRepository
UserServices --> PasswordEncoder
UserServices --> JavaMailSender
GridCellServices --> GridCellRepository
@enduml
```

```plantuml
@startuml
start
:User visits registration page;
:User fills out registration form;
:User submits registration form;

:Server validates form data;
if (Data is valid?) then (yes)
    :Server creates user account;
    :Server sends confirmation email;
    if (User confirms email?) then (yes)
        :Registration successful;
    else (no)
        :Registration failed due to unconfirmed email;
    endif
else (no)
    :Registration failed;
endif

stop
@enduml
```

## Prototyp aplikace

https://www.figma.com/design/w4Fb3M0apUJyaB2PwkqFKG/Untitled?node-id=0-1&t=ok4inraVwJVVuUuQ-1

