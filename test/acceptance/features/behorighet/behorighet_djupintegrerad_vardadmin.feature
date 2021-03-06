# language: sv
@behorighet @djupintegration @vårdadmin
Egenskap: Behörigheter för en djupintegrerad vårdadministratör

Bakgrund: Logga in
	Givet att jag är inloggad som djupintegrerad vårdadministratör

@signera @skriv-ut @klar-för-signering
Scenario: Kan markera som klart för signering men inte signera
	När att vårdsystemet skapat ett intygsutkast för slumpat intyg
    Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget

    Så ska det finnas en knapp för att skriva ut utkastet
    Och ska ett info-meddelande visa "Endast läkare får signera intyget."
	Så visas inte signera knappen

	När jag markerar intyget som klart för signering
	Så ska statusuppdatering "KFSIGN" skickas till vårdsystemet. Totalt: "1"
	När jag skickar en ListCertificateForCareWithQA för patienten och vårdenheten
    Så ska svaret innehålla intyget jag var inne på
    Och ska svaret visa intyghändelse "KFSIGN"

@signera @klar-för-signering-sparat
Scenario: Kan markera som klart för signering men inte signera
	När att vårdsystemet skapat ett intygsutkast för slumpat SMI-intyg
    Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget

    Så ska det finnas en knapp för att skriva ut utkastet
    Och ska ett info-meddelande visa "Endast läkare får signera intyget."
	Så visas inte signera knappen

	När jag markerar intyget som klart för signering
	Och laddar om sidan
	Så ska jag se texten "Utkastet är sparat och markerat som klart för signering."
