# O projektu

Osnovna ideja projekta je prikupljanje podataka i cena novih laptop računara sa sajta [www.ebay.com](www.ebay.com) i korišćenje tih podataka za kreiranje prediktivnog modela koji bi predvideo cenu laptop računara sa određenom specifikacijom. U projektu su korišćene četiri vrste prediktivnih metoda: k-najbližih suseda, linearna regresija, M5P i REPTree. Rezultati ovih metoda su upoređivani kako bi se došlo do zaključka koja od ovih metoda je najpodobnija za ovaj tip predikcije.

# Korišćene metode

### Metoda k-najbližih suseda
Metoda k-najbližih suseda (eng. k-Nearest Neigbours) predstavlja neparametrsku klasifikacionu tehniku koja klasifikuje zadati vektor atributa na osnovu skupa od k najbližih suseda tog vektora. Pri tom se pod najbližim susedima misli na podatke iz trening skupa podataka koji imaju najviši stepen sličnosti vektora atributa sa posmatranim vektorom.[1] Sličnost između atributa se može meriti na dva načina, merom indirektne proporcijonalnosti distance (1/d) i merom komplementa distance (1-d), gde je d distanca izmerena Euklidskom udaljenošću. 

### Linearna regresija
Linearna regresija (eng. Linear Regression) je popularna tehnika koja se često koristi u procesu otkrivanja zakonitosti u podacima (eng. Data Mining). Uopšteno, model koristi jednu ili više nezavisnih promenljivih i predviđa nezavisnu promeljivu kao rezultat. Regresioni model se koristi za predviđanje vrednosti nepoznate promenljive.[2]

### M5P
M5P (eng. M5 model trees) implementira osnovne pristupe za generisanje M5 Model stabla i pravila. Osnovni M5 algoritam je razvio R. Quinlan-ov, a unapredio Yong Wang. M5P kombinuje klasično stablo odlučivanja sa funkcijom linearne regresije u čvorovima. Algoritam koji generiše stablo odlučivanja je korišćen za kreiranje stabla, ali umesto entropije, za svaki unutrašnji čvor kriterijum podele koji se koristi je minimizacija varijanse u svakom podstablu.[3]

### REPTree
REPTree koristi regresiono stablo kako bi kreirao različita stabla u više iteracija. Nakon toga bira najbolje stablo od svih generisanih koje će se smatrati reprezentativnim.[4]

# Skup podataka
Za prikupljanje podataka o novim laptop računarima korišćen je [eBay Finding API](https://go.developer.ebay.com/what-ebay-api) (Listing 1). Prikupljeni podaci su se koristili za dva skupa podataka koji su prikupljeni sa ciljem da se uoči koji bi skup podataka dao bolji predikcioni model. Razlika između ta dva skupa podataka je u vrednosti atributa "processor": u prvom skupu ima više sličnih nominalnih vrednosti, dok su u drugom slične nominalne vrednosti grupisane i označene jednom vrednošću.

Proces prikupljanja podataka uz pomoć navedenog servisa se sastojao iz sledećih koraka:
1) Prikupljeni su ID-jevi proizvoda
2) Korišćenjem prikupljenih ID-jeva - prikupljani su podaci za prvi skup podataka
3) Koriscenjem prikupljenih ID-jeva - prikupljeni su podaci za drugi skup podataka

```
http://svcs.ebay.com/services/search/findingservice/v1?operation-name=finditemsbyproduct&service-version=
1.13.0&SECURITY-APPNAME=freelanc-xxxx-xxxx-xxxx-xxxxxxxxxxxx&RESPONSE-DATA-FORMAT=JSON
&REST-PAYLOAD&categoryId=175672&paginationInput.entriesPerPage=100&paginationInput.pageNumber=x
&itemFilter(0).name=Condition&itemFilter(0).value=1000&itemFilter(1).name=ListingType&itemFilter(1).
value=FixedPrice&aspectFilter(0).aspectName=Processor+Type&aspectFilter(0).aspectValueName(0)=Intel+Xeon
&aspectFilter(0).aspectValueName(1)=AMD+E-Series&aspectFilter(0).aspectValueName(2)=Intel+Atom&
aspectFilter(0).aspectValueName(3)=Intel+Celeron&aspectFilter(0).aspectValueName(4)=Intel+Core+2+Duo&
aspectFilter(0).aspectValueName(5)=Intel+Core+2+Quad&
aspectFilter(0).aspectValueName(6)=Intel+Core+i3+1st+Gen.&
aspectFilter(0).aspectValueName(7)=Intel+Core+i3+2nd+Gen.&
aspectFilter(0).aspectValueName(7)=Intel+Core+i3+3rd+Gen.&
aspectFilter(0).aspectValueName(8)=Intel+Core+i3+4th+Gen.&
aspectFilter(0).aspectValueName(9)=Intel+Core+i5+1st+Gen.&
aspectFilter(0).aspectValueName(10)=Intel+Core+i5+2nd+Gen.&
aspectFilter(0).aspectValueName(11)=Intel+Core+i5+3rd+Gen.&
aspectFilter(0).aspectValueName(12)=Intel+Core+i5+4th+Gen.&
aspectFilter(0).aspectValueName(13)=Intel+Core+i7+1st+Gen.&
aspectFilter(0).aspectValueName(14)=Intel+Core+i7+2nd+Gen.&
aspectFilter(0).aspectValueName(15)=Intel+Core+i7+3rd+Gen.&
aspectFilter(0).aspectValueName(16)=Intel+Core+i7+4th+Gen.&
aspectFilter(0).aspectValueName(17)=Intel+Pentium&
aspectFilter(1).aspectName=Graphics+Processing+Type&
aspectFilter(1).aspectValueName(0)=Dedicated+Graphics&
aspectFilter(1).aspectValueName(1)=Hybrid+Graphics&
aspectFilter(1).aspectValueName(2)=Integrated/On-Board+Graphics&
aspectFilter(2).aspectName=Memory&aspectFilter(0).aspectValueName=1+GB+or+more
```
 Listing 1 - Primer poziva eBay Finding API-ja

 Kategorija *175672* na eBay sajtu predstavlja laptop i netbook računare (Laptops & Netbooks). Takođe, pri pozivu servisa, navedeno je da kod svakog rezultata treba biti deklarisane sledeće specifikacije koje su uzete kao relevantne za ova dva tipa podataka: 
 - tip procesora
 - RAM memorija
 - veličina ekrana
 - tip grafike
 - operacioni sistem
 - da li računar poseduje SSD
 - cena )koja je naglašena da treba da bude fiksna, kako pri prikupljanju podataka u obzir ne bi ulazili podaci o aukcijama koji bi zbog specifičnosti stvaranja cene mogli narušiti konzistentnost skupa podataka)

Obradom prikupljenih podataka napravljena su dva skupa podataka u [.arff](https://weka.wikispaces.com/ARFF+(stable+version)) formatu koji izgledaju kao u Listingu 2 (prvi podskup podataka), onosno Listingu 3 (drugi podstup podataka). Oba skupa podataka sadrže po 878 instanci. 90% skupa podataka, odnosno 790 instanci je korišćeno za treniranje modela, dok je ostatak od 88 instanci korišćen za njihovo testiranje.  
 ```
@ATTRIBUTE processor	{i34th,i33rd,i51st,i52nd,i53rd,i54th,i73rd,i74th,Pentium,Celeron,Atom,AMDSemprom,AMDESeries,Core2Duo,Core2Quad}
@ATTRIBUTE memory		numeric
@ATTRIBUTE screenSize	numeric
@ATTRIBUTE grapics		{Integrated/On-BoardGraphics,DedicatedGraphics}
@ATTRIBUTE operatinS	{Windows10,Windows8,Windows7,WindowsXP,ChromeOS}
@ATTRIBUTE ssd			{Yes,No}
@ATTRIBUTE price		numeric

@DATA
AMDSemprom,4,15.6,Integrated/On-BoardGraphics,Windows7,No,229.99
i74th,16,15.6,DedicatedGraphics,Windows8,Yes,959.96
Celeron,4,15.6,Integrated/On-BoardGraphics,Windows8,No,255
i74th,4 ,12.5,Integrated/On-BoardGraphics,Windows7,Yes,669.95
i54th,4,17.3,Integrated/On-BoardGraphics,Windows10,No,452
```
Listing 2 - Primer podataka u prvom skupu podataka

```
@ATTRIBUTE processor	{i3,i5,i7,Pentium,Celeron,Atom,AMDSemprom,AMDESeries,,Core2Duo,Core2Quad}
@ATTRIBUTE memory		numeric
@ATTRIBUTE screenSize	numeric
@ATTRIBUTE grapics		{Integrated/On-BoardGraphics,DedicatedGraphics}
@ATTRIBUTE operatinS	{Windows10,Windows8,Windows7,WindowsXP,ChromeOS}
@ATTRIBUTE ssd			{Yes,No}
@ATTRIBUTE price		numeric

@DATA
AMDSemprom,4,15.6,Integrated/On-BoardGraphics,Windows7,No,229.99
i7,16,15.6,DedicatedGraphics,Windows8,Yes,959.96
Celeron,4,15.6,Integrated/On-BoardGraphics,Windows8,No,255
i7,4 ,12.5,Integrated/On-BoardGraphics,Windows7,Yes,669.95
i5,4,17.3,Integrated/On-BoardGraphics,Windows10,No,452
```
Listing 3 - Primer podataka u drugom skupu podataka

# Tehnička realizacija

Prikupljanje podataka i njihova obrada je urađena u programskom jeziku PHP. Testiranje prediktivnih modela je implementirano u programskom jeziku Java, korišćenjem biblioteke [Weka](http://www.cs.waikato.ac.nz/ml/weka/). Weka je biblioteka koja sadrži čitavu lepezu algoritama mašinskog učenja koji se koriste za analizu podataka. U pitanju je "open source" softver.[5]
Klase koje su korišćene iz navedene biblioteke su IBk (za k najbližih suseda), LinearRegression (za linearnu regresiju), M5P i REPTree.

# Analiza

Za upoređivanje predikcionih modela numeričke klase treba korišćeni su sledeći parametri: srednja apsolutna greška, relativna apsolutna greška, srednja kvadratna greška i srednja relativna kvadratna greška.

## Srednja apsolutna greška
Apsolutna greška je brojna vrednost i u isto vreme fizička veličina koja opisuje razliku između prave i izmerene vrednosti izražene u jedinicama u kojima je izražena merena vrednost[6]. Njena srednja vrednost pokazuje prosečnu grešku koju ovaj model pravi na bilo kojoj narednoj predikciji. Ova vrednost je pogodna sa analizu jer pokazuje rezervu koja se uzima pri korišćenju modela u narednim predikcijama i to u jedinici u kojoj se vrši merenje.

## Relativna apsolutna greška
Relativna greška je brojna vrednost koja se iskazuje kao udeo (odnos) apsolutne greške u veličini stvarne vrednosti ili srednje vrednosti više merenja.[6] Ovaj parametar se koristi u statističke svrhe i pogodan je za upoređivanje više različitih parametara, s obzirom da se izražava u procentima.

## Srednja kvadratna greška
Srednja kvadradna greška je prosečna vrednost kvadradnog odstupanja izmerene od stvarne vrednosti. Ovaj parametar se često koristi jer sagledava modele rigoroznije od prethodna dva parametra. Veća odstupanja se kvadriranjem "kažnjavaju" i samim tim ovaj parametar je veći kod modela koji ne predviđaju sve uzorke sa ujednačeno malom greškom.

## Relativna kvadratna greška
Kao kod relativne apsolutne greške, ovaj parametar prikazuje udeo kvadratne greške u veličini stvarne vrednosti. Ovaj parametar je važno koristiti kod upoređivanja različitih modela i njihovih srednjih kvadratnih odstupanja.<br>

## ε slučajna greška
Model linearne regresije je jedini algoritam koji ima specifičan parametar pogodan za međusobno upoređivanje više linearnih modela - ε slučajna greška. Upravo zbog toga su prvo analizirana dva dobijena linearna modela trenirana na prikupljenim skupovima podataka.
![linearnimodel1](https://cloud.githubusercontent.com/assets/10245806/17697703/a22141bc-63b6-11e6-8165-15b5a413abc1.png "Linearni model 1")
![linearnimodel2](https://cloud.githubusercontent.com/assets/10245806/17697730/c7aa35ba-63b6-11e6-94a8-b703586eeb46.png "Linearni model 2")

Kod treniranja modela metodom k-najbližih suseda (eng. k-Nearest Neigbours) su korišćena dva različita načina merenja distanci, stoga su i prikazana dva modela trenirana ovom metodom. Model k-najbližih suseda("I") je treniran sa indirektnom proporcijonalnom distancom, dok je model k-najbližih suseda("F") treniran sa merom komplementa distance (1-d).

## Rezultati
Iz funkcija regresionih modela može se uočiti da je ε, slučajna greška, odnosno skrivena promenljiva koja pokazuje neopisanost modela kod prvog modela 166,0519 a drugog 183,657 iz čega se može zaključiti da, što se linearne regresije tiče, bolji model dobijen treniranjem nad prvim skupom podataka.

Na osnovu sledeće tabele mogu se uporediti svi korišćeni modeli: 
![tabela2](https://cloud.githubusercontent.com/assets/10245806/18419098/2b2013ce-7854-11e6-8fdc-b77c8614f765.png "Uporedna tabela")
Uporedna analiza je izvršena na osnovu srednje kvadratne greške. Poređenjem rezultata po modelu po svakom od dva skupa podataka, zaključeno je da svi modeli trenirani nad prvim skupom podataka imaju manju srednju kvadratnu grešku, samim tim je i prvi skup podataka relevantniji za predikciju.

Poređenjem modela treniranim nad prvim skupom podataka može se primetiti da najmanju srednju kvadratnu grešku ima Linearna regresija, sa veoma malom razlikom je sledi M5P model, a zatim REPTree. Poređenjem ova tri modela na drugom skupu podataka primetno je da je situacija malo drugačija, s toga se izvodi zaključak da se u zavisnosti od skupa podataka, preporučuje korišćenje nekog od tri prethodno navedena modela.

Na osnovu svih rezultata i parametara, izveden je zaključak o najboljem modelu za početni problem predikcije cene. S obzirom na prednosti i nedostatke svakog modela, a najviše zbog opasnosti od prevelikog podudaranja (eng. overfitting), odnosno pretreniranja modela nad trening skupom podataka, preporuka je da se koristi M5P model. Ovaj model je takođe predložen zbog konzistentnosti rezultata dobijenih treniranjem modela.

Detaljni rezultati se nalaze u dokumentu ["rezultati.txt"](https://gitlab.com/KrstevFilip/Predicting_laptop_price/blob/master/rezultati.txt).

# Reference
[1] OpenTox, "K Nearest Neighbor", link: http://www.opentox.org/dev/documentation/components/knn , datum pristupa: 11.07.2016. <br>
[2] Oracle documentation, "Regression", link: https://docs.oracle.com/cd/B28359_01/datamine.111/b28129/regress.htm , datum pristupa: 11.07.2016. <br>
[3] Weka documentation, "Class M5P" , link: http://weka.sourceforge.net/doc.dev/weka/classifiers/trees/M5P.html , datum pristupa: 11.07.2016. <br>
[4] Sushilkumar Kalmegh "Analysis of WEKA Data Mining Algorithm REPTree, Simple Cart and RandomTree for Classification of Indian News", link: http://ijiset.com/vol2/v2s2/IJISET_V2_I2_63.pdf , datum pristupa: 11.07.2016. <br>
[5] Weka, "Weka 3: Data Mining Software in Java",  link: http://www.cs.waikato.ac.nz/ml/weka, datum pristupa: 11.07.2016.<br>
[6] Michael Abernethy, "Data mining with WEKA, Part 1: Introduction and regression",  link: http://www.ibm.com/developerworks/library/os-weka1/, datum pristupa: 11.07.2016.<br>
