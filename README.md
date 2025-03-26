# VŠE page

### Předmět
softwarové inženýrství na VŠE, ZS 2024/2025
týmový projekt, kde jsem programovala backend webové applikace inspirované The Million Dollar Homepage a pomáhala jsem s propjením backendu a frontendu, který je udělán pomocí HTML s vloženými scripty v JavaScriptu.

### Použité technologie
Tato webová aplikace je postavená na Spring Bootu, s využitím Spring Security pro správu uživatelských rolí a zabezpečení přístupu. K ukládání dat aplikace využívá MySQL databázi, se kterou komunikuje pomocí Spring Data JPA. Frontend je implementován pomocí Thymeleaf, což umožňuje dynamické generování HTML stránek přímo na serveru.

Dále aplikace obsahuje:
Spring Boot Web & Web Services – pro tvorbu webových endpointů a případnou komunikaci přes webové služby.
Spring Boot Mail – pro odesílání e-mailů (např. potvrzovací zprávy nebo notifikace).
Lombok – pro zjednodušení práce s datovými třídami pomocí anotací (např. @Getter, @Setter).

Celý projekt běží na Java 17 a je spravován pomocí Maven.

## Anotace
Webová stránka s omezeným množstvím pixelů, které si lidé mohou koupit za symbolickou cenu 1 pixel za 1 korunu. Koupené pixely je možné zabarvit a vytvářet obrázky nebo slova, která se budou zobrazovat na domovské stránce. Stránka je zaměřena na studenty VŠE, ale nebrání se ani propagaci firem. Pixely si ale každý kupuje pouze na omezenou dobu. Po skončení období, jsou vybrané peníze odevzdány studentům z fakulty, která měla nejvíce pixelů. Aktuální skóre je možné sledovat na leaderboardu.

## Základní funkcionalita
Skutečná platební brána není implementována.
Identifikujeme čtyři role uživatelů s různými přistupy. (unverified, user, admin, nezaregistrovaný)
Lidé si mohou vytvořit uživatelský profil, který je spojen a ověřen se školní emailovou adresou a fakultou VŠE.
Registrovaní uživatelé mohou kopit množství pixelů a libovolně je zbarvit.
Všichni uživatelé mohou sledovat scoreboard, který zobrazuje, která fakulta aktuálně nakoupila nejvíce pixelů v daném období. Dále mohou sledovat obrázek na homepage.
Moderátoři mají možnost mazat obsah nesplňující stanovené podmínky.
![Usecases diagram](https://adela-domokosova.github.io/pictures/vse_page/uml/use_cases.png)

## Databáze
Byla použita MySQL databáze, která byla hostována na AWS. V src/main/resources/application.properties je nastaveno připojení k databázi.
Pro vytvoření databáze nebyly použity scripty, místo toho byla každá tabulka vytvořena jako entity pomocí Spring Data JPA a Hibernate. databáze se generuje podle dat v Entity balíčku, obsahuje tabulku uživatelů, pixelů a transakcí. S databází komunikujeme pomocí Repositories.
![Původní návrh databáze](https://adela-domokosova.github.io/pictures/vse_page/uml/db_navrh.png)

## Návrh byckendu - třívrstvá struktura
Záměr byl, aby aplikace na backendu měla tři vrstvy, které vzájemně komunikují předem daným způsobem:
1. Controller Layer (Kontrolery) – Zpracovává HTTP požadavky a vrací odpovědi, metody Service Layer
2. Service Layer (Služby) – Obsahuje obchodní logiku aplikace, bere informace z databáze pomocí Repository Layer a předává je Controller Layer.
3. Repository Layer (Úložiště) – Komunikuje s databází přes JPA/Hibernate.

Prakticky pro uživatele admina to znamená, že požadavky bude zpracovávat AdminController, Logiku bude brát převážně z GridCellServices a UserServices, které budou komunikovat s databází pomocí GridCellRepository a MyUserRepository.
![Třívrstvá struktura aplikace pro uživatele admin](https://adela-domokosova.github.io/pictures/vse_page/uml/admin_classes.png)
**Zlepšení 1** vrstvi nejsou ve skutečnosti tak strikně oddělené a Controller tak někdy komunikuje přímo s repozitářem. Při této velikosti projektu, to příliš velký problém nebyl, ale při dlouhodobé údržbě webu nebo jeho rozrůstání by se kó stával více nepřehledný a hůže udržovaný.
**Zlepšení 2** zbytečně vzniklo více Services pro práci s uživatelskými daty

## Featury
### Email confirmation
Při návrhu aplikace se předpokládalo, že do aplikace se mohou registrovat pouze studenti VŠE, což bylo zajištěno několika způsoby.
1. Validace emailové adresy při registraci
- očekává se adresa s @vse.cz
- předpokládá se, že prvních 6 znaků bude studentský xname, který je přidělen každému studentovi VŠE. Tím se pak vytvoří unikátní uživatelské jméno.
** Zlepšení** validace je na frontendu - jednoduše obejitelné

2. Potvrzení emailové adresy
- Při registraci se vytvoří nový uživatel v databázi s verifikačním kódem
- Také se odešle verifikační email na uvedenou emailovou adresu, ta obsahuje link na [url]/verify?code=[user_verification_code]
- kliknutím na link v emailu se ověří verifikační kód a uživateli je v databázi upravena role
**Zlepšení** pridat omezeny cas na verifikaci uctu

## Prototyp aplikace
![Figma - scoreboard](https://adela-domokosova.github.io/pictures/vse_page/figma/scoreboard_figma.png)
![Figma - payment page](https://adela-domokosova.github.io/pictures/vse_page/figma/payment_page_figma.png)
![Figma_login](https://adela-domokosova.github.io/pictures/vse_page/figma/login_figma.png)
