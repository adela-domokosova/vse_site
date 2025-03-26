# VŠE page



Webová stránka s omezeným množstvím pixelů, které si lidé mohou koupit za symbolickou cenu 1 pixel za 1 korunu. Koupené pixely je možné zabarvit a vytvářet obrázky nebo slova, která se budou zobrazovat na domovské stránce. Stránka je zaměřena na studenty VŠE, ale nebrání se ani propagaci firem. Pixely si ale každý kupuje pouze na omezenou dobu. Po skončení období, jsou vybrané peníze odevzdány studentům z fakulty, která měla nejvíce pixelů. Aktuální skóre je možné sledovat na leaderboardu.

## Funkcionalita

Lidé si mohou vytvořit uživatelský profil, který je spojen a ověřen se školní emailovou adresou a fakultou VŠE.

Registrovaní uživatelé mohou kopit množství pixelů a libovolně je zbarvit, ale pouze na omezenou dobu.

Všichni uživatelé mohou sledovat scoreboard, který zobrazuje, která fakulta aktuálně nakoupila nejvíce pixelů v daném období. Dále mohou sledovat obrázek na homepage.

Moderátoři mají možnost mazat obsah nesplňující stanovené podmínky.

Uživatelé



Databáze
Byla použita MySQL databáze, která byla hostována na AWS. V src/main/resources/application.properties je nastaveno připojení k databázi.
![Původní návrh databáze](https://adela-domokosova.github.io/pictures/vse_page/uml/db_navrh.png)

Entity
Pro vytvoření databáze nebyly použity scripty, místo toho byla každá tabulka vytvořena jako entity pomocí Spring Data JPA a Hibernate. databáze se generuje podle dat v Entity balíčku, obsahuje tabulku uživatelů, pixelů a transakcí. S databází komunikujeme pomocí Repositories.

Repository
Každým zápisem do jedné z tabulek vzniká nová instance entity. S těmito instancemi pak manipuluji použitím repository interfaces. Interface obsahuje metody pro hledání specifických entit, jako je findByUsername(String name).


Návrh byckendu - třívrstvá struktura
Záměr byl, aby aplikace na backendu měla tři vrstvy, které vzájemně komunikují předem daným způsobem:
1. Controller Layer (Kontrolery) – Zpracovává HTTP požadavky a vrací odpovědi, metody Service Layer
2. Service Layer (Služby) – Obsahuje obchodní logiku aplikace, bere informace z databáze pomocí Repository Layer a předává je Controller Layer.
3. Repository Layer (Úložiště) – Komunikuje s databází přes JPA/Hibernate.


Admin
Prakticky pro uživatele admina to znamená, že požadavky bude zpracovávat AdminController, Logiku bude brát převážně z GridCellServices a UserServices, které budou komunikovat s databází pomocí GridCellRepository a MyUserRepository.
![Třívrstvá struktura aplikace pro uživatele admin](https://adela-domokosova.github.io/pictures/vse_page/uml/admin_classes.png)
**Zlepšení 1** vrstvi nejsou ve skutečnosti tak strikně oddělené a Controller tak někdy komunikuje přímo s repozitářem. Při této velikosti projektu, to příliš velký problém nebyl, ale při dlouhodobé údržbě webu nebo jeho rozrůstání by se kó stával více nepřehledný a hůže udržovaný.
**Zlepšení 2** zbytečně vzniklo více Services pro práci s uživatelskými daty


Návrh backendu - třívrstvá struktura



Users, hashovani hesel

api navrh





emaily na confirmation



## Prototyp aplikace

https://www.figma.com/design/w4Fb3M0apUJyaB2PwkqFKG/Untitled?node-id=0-1&t=ok4inraVwJVVuUuQ-1

