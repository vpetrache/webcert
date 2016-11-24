# language: sv
@djupintegration @SMI
Egenskap: Djupintegration SMI intyg

Bakgrund: Jag är inloggad som djupintegrerad läkare
   Givet att jag är inloggad som djupintegrerad läkare på vårdenhet "TSTNMT2321000156-1004"

@namnbyte
Scenario: Informera om patienten har bytt namn
	När att vårdsystemet skapat ett intygsutkast för slumpat intyg 
    Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
    Och jag går in på intygsutkastet via djupintegrationslänk med annat namn
    Så ska ett info-meddelande visa "Observera att patientens namn har ändrats sedan det här intyget utfärdades."

@adressbyte @nyttIntyg
Scenario: Informera om patienten har bytt adress och använd address på nya intyg
	När att vårdsystemet skapat ett intygsutkast för slumpat intyg
	Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
    Och jag går in på intygsutkastet via djupintegrationslänk med annan adress
    Så ska ett info-meddelande visa "Observera att patientens adress har ändrats sedan det här intyget utfärdades."

    När jag kopierar intyget
    Och jag signerar intyget
    Så ska intyget visa den nya addressen

@samordningsnummer
Scenario: Informera om patienten har fått ett nytt personnummer
	När att vårdsystemet skapat ett intygsutkast för slumpat intyg med samordningsnummer
	Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
    Och jag går in på intygsutkastet via djupintegrationslänk med ett annat personnummer
    Så ska ett varning-meddelande visa "Patienten har ett nytt personnummer"

@reservnummer
Scenario: Informera om patienten har fått ett reservnummer
	När att vårdsystemet skapat ett intygsutkast för slumpat intyg med samordningsnummer
	Och jag går in på intygsutkastet via djupintegrationslänk
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
    Och jag går in på intygsutkastet via djupintegrationslänk med ett reservnummer
    Så ska ett varning-meddelande visa "Patienten har samordningsnummer kopplat till reservnummer"