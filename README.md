# SpinSpot

## Projektni tim 

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa 
------------  | ------------------- | ----- | ----------------------| ----------------
Emanuel Valec | evalec21@foi.hr | 0016156391 | evalec21 | G02 
Dominik Černjević | dcernjevi21@foi.hr | 0016155459 | dcernjevi21 | G02 
Nikola Huzjak | nhuzjak20@foi.hr | 0016147728 | nhuzjak20 | G02 


## Opis domene 

DJ-i imaju ključnu ulogu u stvaranju atmosfere na raznim glazbenim eventima, festivalima, klubovima i privatnim zabavama. Korisnici koji prisustvuju događanjima često traže način da brzo i lako pronađu informacije o nadolazećim glazbenim događajima, DJ-ima, lokacijama i mogućnosti interakcije, kao što je naručivanje pjesama tijekom nastupa. 
 
Problem se očituje u nedostatku jedinstvene platforme koja omogućava DJ-evima da na jednom mjestu efikasno upravljaju svojim angažmanima, financijama, promocijom i interakcijom s publikom, te koji istovremeno nude publici jednostavan pristup relevantnim informacijama i uslugama vezanim uz glazbene događaje. 


## Specifikacija projekta 

Opišite osnovnu buduću arhitekturu programskog proizvoda. Obratite pozornost da mobilne aplikacije često zahtijevaju pozadinske servise. Priložite odgovarajuće dijagrame i skice gdje je to prikladno.  

| Naziv      | Oznaka | Kratki Opis | Odgovoran član tima |
| ----------- | ----------- | ----| ---------------------|
| F02 | Evidencija DJ angažmana | DJ može unositi podatke o svojim nastupima (datum, lokaciju, vrijeme i honorar. Unosom angažmana omogućuje mu se pregled statistike. | Dominik Černjević |
| F05 | Export DJ statistike | DJ može izvesti izvještaj svojih gaža i financijskih podataka u PDF formatu i uz to DJ može pregledavati statistiku za broj gaža kroz proizvoljno vremensko razdoblje, zarade, najčešći angažmani i sl. | Dominik Černjević |
| F07 | Izvještavanje korisnika o budućim događajima | Korisnici mogu postaviti notifikacije za nadolazeće DJ nastupe ili događanja prema njihovim interesima (omiljeni DJ, omiljeni žanr). | Dominik Černjević |
| API | API | Izraditi API za komuniciranje aplikacije s remote bazom | Dominik Černjević |
| F01      | Registracija i prijava | Za pristup aplikaciji prvo treba stvoriti korisnički račun tako što korisnik upisuje valjanu e-mail adresu i lozinku koji se spremaju u bazu podataka. Korisniku se šalje e-mail za potvrdu računa uz sve to za pristup aplikaciji potrebna je autentikacija korisnika pomoću login funkcionalnosti. Korisnik se logira s podacima koje si je dodijelio prilikom registracije u aplikaciju | Emanuel Valec |
| F03| Kalendar događanja | DJ može pregledavati i uređivati kalendar svojih nastupa, dok korisnici mogu pretraživati događaje po datumu, lokaciji ili DJ-u. | Nikola Huzjak |
| F04| Pretraživanje DJ-a i događanja | Korisnici mogu pretraživati DJ profile prema različitim kriterijama (naziv, lokacija, žanr). Prikazivanje informacija uključuje popis događa i biografije DJ-eva. | Nikola Huzjak |
| F06 | Recenziranje DJ-eva | Korisnici mogu ostavljati recenzije i ocjene za DJ-eve nakon njihovih nastupa, a DJ-evi mogu pratiti povratne informacije kako bi se poboljšali.| Emanuel Valec |
| F08 | Plaćanje za narudžbu pjesme Paypalom | Korisnici mogu naručiti pjesmu od DJ-a tijekom nastupa putem aplikacije i izvršiti online plaćanje preko Paypala. Prije nego se obavi prijenos sredstava DJ mora odobriti zahtjev, ukoliko odbije rezervirana sredstva sve otključavaju korisniku. | Nikola Huzjak |
| F09 | Osnovna implementacija profila | DJ može na svoj profil postaviti profilnu sliku i baner, dodati svoju biografiju, žanrove koje svira i sl. Korisnik na svojem profilu može postaviti profilnu sliku te preference i sl. | Emanuel Valec |


## Tehnologije i oprema 

Razvojno okruženje: Android studio

Programski jezik: Kotlin

Baza podataka: MySQL

Verzioniranje koda: Git i GitHub

Upravljanje zadacima: GitHub Projects

Obrada platnih transakcija: Stripe ili Paypal

 

## Baza podataka i web server 

Tražimo pristup serveru na kojemu ćemo moći imati bazu podataka.

## .gitignore 

Uzmite u obzir da je u mapi Software .gitignore konfiguriran za nekoliko tehnologija, ali samo ako će projekti biti smješteni direktno u mapu Software ali ne i u neku pod mapu. Nakon odabira konačne tehnologije i projekta obavezno dopunite/premjestite gitignore kako bi vaš projekt zadovoljavao kriterije koji su opisani u ReadMe.md dokumentu dostupnom u mapi Software. 
