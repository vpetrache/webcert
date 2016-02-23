# language: sv
@behorighet
# PRIVILEGE_NAVIGERING
Egenskap: Behörigheter för en djupintegrerad läkare

@djup_lakare
Scenario: Kan makulera sjukintyg
	Givet att jag är inloggad som djupintegrerad läkare
	När jag väljer patienten "19520727-2252"
	Och jag går in på ett "Läkarintyg FK 7263" med status "Signerat"
	Och jag makulerar intyget
	Så ska intyget visa varningen "Begäran om makulering skickad till intygstjänsten"

@djup_lakare
Scenario: Kan kopiera och signera ett (kopierat)intyg
	Givet att jag är inloggad som djupintegrerad läkare
	När jag väljer patienten "19520727-2252"
	Och jag går in på ett "Läkarintyg FK 7263" med status "Signerat"
	Och jag kopierar intyget
	Och jag signerar intyget
	Så ska intygets status vara "Intyget är signerat"

@djup_lakare @notReady
Scenario: Kan kopiera ett intyg från tidigare intyg listan (utan att gå in i intyget)
	Givet att jag är inloggad som djupintegrerad läkare
	När jag väljer patienten "19520727-2252"
	Och kopierar ett signerat intyg
	Så ska intygets status vara "Intyget är signerat"

# PRIVILEGE_BESVARA_KOMPLETTERINGSFRAGA
@djup_lakare @notReady
Scenario: Djupintegrerad läkare besvarar kompleterings fråga

@djup_lakare @notReady
Scenario: Djupintegrerad läkare besvarar kompleterings fråga
   Givet att jag är inloggad som djupintegrerad läkare
   När jag väljer patienten "19520727-2252"
   Och jag går in på ett "Läkarintyg FK 7263" med status "Mottaget"
   Och Försäkringskassan ställer en "Kontakt" fråga om intyget
   Och jag svarar på frågan
   Så ska frågan vara hanterad