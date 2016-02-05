# language: sv
@behorighet
Egenskap: Behörigheter för en uthoppsläkare

@impl
Scenario: En uthoppsläkare ska kunna signera intyg
   Givet att jag är inloggad som uthoppsläkare
   När jag väljer patienten "19971019-2387"
   Och jag går in på att skapa ett "Läkarintyg FK 7263" intyg
   Och jag fyller i alla nödvändiga fält för intyget
   Så är signeraknappen tillgänglig
   När jag signerar intyget
   Så ska jag se den data jag angett för intyget

   
   

