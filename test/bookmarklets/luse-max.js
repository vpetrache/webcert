var today = new Date();
var todayDateString = today.getFullYear() + '-' + ('0' + (today.getMonth() + 1)).slice(-2)  + '-' + ('0' + today.getDate()).slice(-2);

$("input[id*='date_undersokningAvPatienten_']").click();
$("input[id*='_date_journaluppgifter_']").click();
$("input[id*='_date_anhorigsBeskrivningAvPatienten_']").click();
$("input[id*='_date_annatGrundForMU_']").click();
$("input[id*='_single-text_annatGrundForMUBeskrivning_']").val('Journal fran Dalarnas lan').change();
$("input[id*='date_kannedomOmPatient_']").click();
$("#underlagFinnsYes").prop("checked", true);
$("#underlagFinnsYes").click();
$("#underlag-0-typ").val('string:PSYKOLOG').change();
$("#underlag-0-datum").val(todayDateString).change();
$("#underlag-0-hamtasFran").val('CSK').change();
$("button:contains('ytterligare underlag')").click();
$("#underlag-1-typ").val('string:PSYKOLOG').change();
$("#underlag-1-datum").val(todayDateString).change();
$("#underlag-1-hamtasFran").val('CSK').change();
$("button:contains('ytterligare underlag')").click();
$("#underlag-2-typ").val('string:!!!PSYKOLOG').change();
$("#underlag-2-datum").val(todayDateString).change();
$("#underlag-2-hamtasFran").val('CSK').change();
$("button:contains('ytterligare underlag')").click();
$("#underlag-3-typ").val('string:PSYKOLOG').change();
$("#underlag-3-datum").val(todayDateString).change();
$("#underlag-3-hamtasFran").val('CSK').change();
$("button:contains('ytterligare underlag')").click();
$("#underlag-4-typ").val('string:PSYKOLOG').change();
$("#underlag-4-datum").val(todayDateString).change();
$("#underlag-4-hamtasFran").val('CSK').change();
$("#sjukdomsforlopp").val("Patienten har haft besvären i olika omgångar och aldrig riktigt fått vara helt frisk, Patienten känner sig trött och orkeslös efter alla års sjukdom. Forsta insjuknandet inföll sig vid 6 års ålder och har sedan dess pågått.");
$("#sjukdomsforlopp").change();
$("#diagnoseCode").val("S47").change().blur();
$("#diagnoseDescription").val("Klämskada på skuldra och överarm").change().blur();
$("a:contains('Lägg till övriga diagnoser')").click();
$("input[id*='diagnoseCode']").eq(1).val('F000').change().blur();
$("input[id*='diagnoseDescription']").eq(1).val('Demens vid Alzheimers sjukdom med tidig debut').change().blur();
$("a:contains('Lägg till övriga diagnoser')").click();
$("input[id*='diagnoseCode']").eq(2).val('G000').change().blur();
$("input[id*='diagnoseDescription']").eq(2).val('Meningit orsakad av Haemophilus influenzae').change().blur();
$("#diagnosgrund").val("I tidernas begynnelse ställdes diagnosen för patienten på Sahlgrenska").change();
$("#nyBedomningDiagnosgrundNo").prop("checked", true);
$("#nyBedomningDiagnosgrundNo").click();
$("#funktionsnedsattningIntellektuell").val("Pga smärtan påverkas intellektet").change();
$("#funktionsnedsattningKommunikation").val("Får svårt att kommunicera då humöret inte är på topp").change();
$("#funktionsnedsattningKoncentration").val("Har svårt att koncentrera sig längre än 2 minuter").change();
$("#funktionsnedsattningPsykisk").val("Påverkar inget annat enligt patienten").change();
$("#funktionsnedsattningSynHorselTal").val("Har fått sämre syn med åldern").change();
$("#funktionsnedsattningBalansKoordination").val("Det gör ont").change();
$("#funktionsnedsattningAnnan").val("Ställer till det en hel del").change();
$("#aktivitetsbegransning").val("Patienten kan inte räcka upp armen. Stelhet i axelpartiet").change();
$("#avslutadBehandling").val("Rheabträning").change();
$("#pagaendeBehandling").val("Smärtlindring och akupunktur").change();
$("#planeradBehandling").val("För tidigt att säga något om planerade åtgärder").change();
$("#substansintag").val("Smärtlindring").change();
$("#medicinskaForutsattningarForArbete").val("Rehabträning ska ge resultat om 1 år").change();
$("#formagaTrotsBegransning").val("Laga mat går bra, men endast på fredagar").change();
$("#ovrigt").val("Mycket material är hemligstämplat").change();
$("input[id*='kontaktMedFk']").click();
$("#anledningTillKontakt").val("Jag har hemligt material på mitt skrivbord som kan vara intressant för er").change();
