# language: sv
@sekretess
Egenskap: Sekretessmarkerad patient

@varningsmeddelande
Scenario: Varningmeddelanden när man går in på patient
   Givet att jag är inloggad som läkare
   Och jag går in på en patient med sekretessmarkering
   Så ska en varningsruta innehålla texten "Patienten har en sekretessmarkering."
   Och ska en varningsruta innehålla texten "På grund av sekretessmarkeringen går det inte att kopiera intyg."

@kopiera @fornya
Scenario: Kan inte kopiera eller förnya ett intyg på en sekretessmarkerad patient
	Givet att jag är inloggad som läkare
   	Och jag går in på en patient med sekretessmarkering
	När jag skickar ett "Läkarintyg FK 7263" intyg till Intygstjänsten
	Och jag går in på intyget
	Så ska det inte finnas en knapp med texten "Kopiera"
	Och ska det inte finnas en knapp med texten "Förnya"
	Och ska en varningsruta innehålla texten "Patienten har en sekretessmarkering."
   	Och ska en varningsruta innehålla texten "På grund av sekretessmarkeringen går det inte att kopiera intyget."

