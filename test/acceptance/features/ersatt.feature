#language: sv

@ersatt
Egenskap: Funktionallitet som rör ersätt

Bakgrund: Jag befinner mig på webcerts förstasida
	Givet att jag är inloggad som läkare
    När jag går in på en patient

@ersatta-btn
Scenario: Finns ersätta-knappen
    När jag går in på att skapa ett slumpat intyg
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
    Så ska jag se en knapp med texten "Ersätt"

@ersatta-text
Scenario: När man klickat på ersätta-knappen så ska följande text visas
	När jag går in på att skapa ett slumpat intyg
    Och jag fyller i alla nödvändiga fält för intyget
    Och jag signerar intyget
	Och klickar på ersätta knappen
	Så ska meddelandet som visas innehålla texten "Ett intyg kan ersättas om det innehåller felaktiga uppgifter eller om ny information tillkommit efter att intyget utfärdades."
