javascript:(function()%7Bvar%20today%20%3D%20new%20Date()%3Bvar%20todayDateString%20%3D%20today.getFullYear()%20%2B%20'-'%20%2B%20('0'%20%2B%20(today.getMonth()%20%2B%201)).slice(-2)%20%20%2B%20'-'%20%2B%20('0'%20%2B%20today.getDate()).slice(-2)%3B%24(%22input%5Bid*%3D'date_undersokningAvPatienten_'%5D%22).click()%3B%24(%22input%5Bid*%3D'_date_journaluppgifter_'%5D%22).click()%3B%24(%22input%5Bid*%3D'_date_anhorigsBeskrivningAvPatienten_'%5D%22).click()%3B%24(%22input%5Bid*%3D'_date_annatGrundForMU_'%5D%22).click()%3B%24(%22input%5Bid*%3D'_single-text_annatGrundForMUBeskrivning_'%5D%22).val('Journal%20fran%20Dalarnas%20lan').change()%3B%24(%22input%5Bid*%3D'date_kannedomOmPatient_'%5D%22).click()%3B%24(%22%23underlagFinnsYes%22).prop(%22checked%22%2C%20true)%3B%24(%22%23underlagFinnsYes%22).click()%3B%24(%22%23underlag-0-typ%22).val('number%3A1').change()%3B%24(%22%23underlag-0-datum%22).val(todayDateString).change()%3B%24(%22%23underlag-0-hamtasFran%22).val('CSK').change()%3B%24(%22button%3Acontains('ytterligare%20underlag')%22).click()%3B%24(%22%23underlag-1-typ%22).val('number%3A1').change()%3B%24(%22%23underlag-1-datum%22).val(todayDateString).change()%3B%24(%22%23underlag-1-hamtasFran%22).val('CSK').change()%3B%24(%22button%3Acontains('ytterligare%20underlag')%22).click()%3B%24(%22%23underlag-2-typ%22).val('number%3A1').change()%3B%24(%22%23underlag-2-datum%22).val(todayDateString).change()%3B%24(%22%23underlag-2-hamtasFran%22).val('CSK').change()%3B%24(%22button%3Acontains('ytterligare%20underlag')%22).click()%3B%24(%22%23underlag-3-typ%22).val('number%3A1').change()%3B%24(%22%23underlag-3-datum%22).val(todayDateString).change()%3B%24(%22%23underlag-3-hamtasFran%22).val('CSK').change()%3B%24(%22button%3Acontains('ytterligare%20underlag')%22).click()%3B%24(%22%23underlag-4-typ%22).val('number%3A1').change()%3B%24(%22%23underlag-4-datum%22).val(todayDateString).change()%3B%24(%22%23underlag-4-hamtasFran%22).val('CSK').change()%3B%24(%22%23sjukdomsforlopp%22).val(%22Patienten%20har%20haft%20besv%C3%A4ren%20i%20olika%20omg%C3%A5ngar%20och%20aldrig%20riktigt%20f%C3%A5tt%20vara%20helt%20frisk%2C%20Patienten%20k%C3%A4nner%20sig%20tr%C3%B6tt%20och%20orkesl%C3%B6s%20efter%20alla%20%C3%A5rs%20sjukdom.%20Forsta%20insjuknandet%20inf%C3%B6ll%20sig%20vid%206%20%C3%A5rs%20%C3%A5lder%20och%20har%20sedan%20dess%20p%C3%A5g%C3%A5tt.%22)%3B%24(%22%23sjukdomsforlopp%22).change()%3B%24(%22%23diagnoseCode%22).val(%22S47%22).change().blur()%3B%24(%22%23diagnoseDescription%22).val(%22Kl%C3%A4mskada%20p%C3%A5%20skuldra%20och%20%C3%B6verarm%22).change().blur()%3B%24(%22a%3Acontains('L%C3%A4gg%20till%20%C3%B6vriga%20diagnoser')%22).click()%3B%24(%22input%5Bid*%3D'diagnoseCode'%5D%22).eq(1).val('F000').change().blur()%3B%24(%22input%5Bid*%3D'diagnoseDescription'%5D%22).eq(1).val('Demens%20vid%20Alzheimers%20sjukdom%20med%20tidig%20debut').change().blur()%3B%24(%22a%3Acontains('L%C3%A4gg%20till%20%C3%B6vriga%20diagnoser')%22).click()%3B%24(%22input%5Bid*%3D'diagnoseCode'%5D%22).eq(2).val('G000').change().blur()%3B%24(%22input%5Bid*%3D'diagnoseDescription'%5D%22).eq(2).val('Meningit%20orsakad%20av%20Haemophilus%20influenzae').change().blur()%3B%24(%22%23diagnosgrund%22).val(%22I%20tidernas%20begynnelse%20st%C3%A4lldes%20diagnosen%20f%C3%B6r%20patienten%20p%C3%A5%20Sahlgrenska%22).change()%3B%24(%22%23nyBedomningDiagnosgrundNo%22).prop(%22checked%22%2C%20true)%3B%24(%22%23nyBedomningDiagnosgrundNo%22).click()%3B%24(%22%23funktionsnedsattningIntellektuell%22).val(%22Pga%20sm%C3%A4rtan%20p%C3%A5verkas%20intellektet%22).change()%3B%24(%22%23funktionsnedsattningKommunikation%22).val(%22F%C3%A5r%20sv%C3%A5rt%20att%20kommunicera%20d%C3%A5%20hum%C3%B6ret%20inte%20%C3%A4r%20p%C3%A5%20topp%22).change()%3B%24(%22%23funktionsnedsattningKoncentration%22).val(%22Har%20sv%C3%A5rt%20att%20koncentrera%20sig%20l%C3%A4ngre%20%C3%A4n%202%20minuter%22).change()%3B%24(%22%23funktionsnedsattningPsykisk%22).val(%22P%C3%A5verkar%20inget%20annat%20enligt%20patienten%22).change()%3B%24(%22%23funktionsnedsattningSynHorselTal%22).val(%22Har%20f%C3%A5tt%20s%C3%A4mre%20syn%20med%20%C3%A5ldern%22).change()%3B%24(%22%23funktionsnedsattningBalansKoordination%22).val(%22Det%20g%C3%B6r%20ont%22).change()%3B%24(%22%23funktionsnedsattningAnnan%22).val(%22St%C3%A4ller%20till%20det%20en%20hel%20del%22).change()%3B%24(%22%23aktivitetsbegransning%22).val(%22Patienten%20kan%20inte%20r%C3%A4cka%20upp%20armen.%20Stelhet%20i%20axelpartiet%22).change()%3B%24(%22%23avslutadBehandling%22).val(%22Rheabtr%C3%A4ning%22).change()%3B%24(%22%23pagaendeBehandling%22).val(%22Sm%C3%A4rtlindring%20och%20akupunktur%22).change()%3B%24(%22%23planeradBehandling%22).val(%22F%C3%B6r%20tidigt%20att%20s%C3%A4ga%20n%C3%A5got%20om%20planerade%20%C3%A5tg%C3%A4rder%22).change()%3B%24(%22%23substansintag%22).val(%22Sm%C3%A4rtlindring%22).change()%3B%24(%22%23medicinskaForutsattningarForArbete%22).val(%22Rehabtr%C3%A4ning%20ska%20ge%20resultat%20om%201%20%C3%A5r%22).change()%3B%24(%22%23formagaTrotsBegransning%22).val(%22Laga%20mat%20g%C3%A5r%20bra%2C%20men%20endast%20p%C3%A5%20fredagar%22).change()%3B%24(%22%23ovrigt%22).val(%22Mycket%20material%20%C3%A4r%20hemligst%C3%A4mplat%22).change()%3B%24(%22input%5Bid*%3D'kontaktMedFk'%5D%22).click()%3B%24(%22%23anledningTillKontakt%22).val(%22Jag%20har%20hemligt%20material%20p%C3%A5%20mitt%20skrivbord%20som%20kan%20vara%20intressant%20f%C3%B6r%20er%22).change()%7D)()