USE g4w16;

DROP TABLE IF EXISTS `Admin`;
DROP TABLE IF EXISTS Poll;
DROP TABLE IF EXISTS Feed;
DROP TABLE IF EXISTS Banner;
DROP TABLE IF EXISTS TaxeRates;
DROP TABLE IF EXISTS SalesDetails;
DROP TABLE IF EXISTS Sales;
DROP TABLE IF EXISTS BookContributors;
DROP TABLE IF EXISTS BookIdentifiers;
DROP TABLE IF EXISTS BookGenres;
DROP TABLE IF EXISTS BookFormats;
DROP TABLE IF EXISTS Contributor;
DROP TABLE IF EXISTS ContributionType;
DROP TABLE IF EXISTS Genre;
DROP TABLE IF EXISTS IdentifierType;
DROP TABLE IF EXISTS Format;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS BillingAddress;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Title;
DROP TABLE IF EXISTS Province;

CREATE TABLE Province (
  ID varchar(128) PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Title (
  ID varchar(128) PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Client (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Title varchar(128) NOT NULL,
  FirstName varchar(128) NOT NULL,
  LastName varchar(128) NOT NULL,
  Email varchar(128) NOT NULL UNIQUE,
  Password varchar(254) NOT NULL,
  CompanyName varchar(128) NOT NULL DEFAULT "",
  homePhoneNumber varchar(20) NOT NULL,
  cellPhoneNumber varchar(20) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE BillingAddress (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(128) NOT NULL DEFAULT "",
  Client int NOT NULL,
  Province varchar(128) NOT NULL,
  City varchar(128) NOT NULL,
  FirstCivicAddress varchar(250) NOT NULL,
  SecondCivicAddress varchar(250) NOT NULL DEFAULT "",
  PostalCode varchar(128) NOT NULL,
  Removed BOOLEAN NOT NULL DEFAULT 0,
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Books (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Title varchar(128) NOT NULL,
  Publisher varchar(128) NOT NULL,
  Description MEDIUMTEXT NOT NULL,
  Pages int NOT NULL,
  WholesalePrice DECIMAL(12,2) NOT NULL DEFAULT 0,
  ListPrice DECIMAL(12,2) NOT NULL,
  SalePrice DECIMAL(12,2) NOT NULL DEFAULT 0,
  PubDate timestamp NOT NULL DEFAULT 0,
  DateEntered timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  RemovalStatus BOOLEAN NOT NULL DEFAULT 0
)ENGINE=InnoDB;


CREATE TABLE Reviews (
  Book int NOT NULL,
  CreationDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Client int NOT NULL,
  Rating int(1) NOT NULL,
  Text MEDIUMTEXT NOT NULL,
  Approval BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (Book, Client),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE ContributionType (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Contributor (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(128) NOT NULL,
  Contribution varchar(128) NOT NULL,
  FOREIGN KEY(Contribution) REFERENCES ContributionType(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE BookContributors (
  Book int NOT NULL,
  Contributor int NOT NULL,
  PRIMARY KEY (Book, Contributor),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Contributor) REFERENCES Contributor(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE IdentifierType (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookIdentifiers (
  Book int NOT NULL,
  Type varchar(128) NOT NULL,
  Code  varchar(128) NOT NULL UNIQUE,
  PRIMARY KEY (Book, Type),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Type) REFERENCES IdentifierType(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Genre (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookGenres (
  Book int NOT NULL,
  Genre varchar(128) NOT NULL,
  PRIMARY KEY (Book, Genre),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Genre) REFERENCES Genre(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Format (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookFormats (
  Book int NOT NULL,
  Format varchar(128) NOT NULL,
  File varchar(254) NOT NULL,
  PRIMARY KEY (Book, Format),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Format) REFERENCES Format(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Sales (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  DateEntered timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Client int NOT NULL,  
  GrossValue DECIMAL(12,2) NOT NULL DEFAULT 0, 
  NetValue DECIMAL(12,2) NOT NULL DEFAULT 0,
  Removed BOOLEAN NOT NULL DEFAULT 0,
  BillingAddress int NOT NULL,
  FOREIGN KEY(BillingAddress) REFERENCES BillingAddress(ID),
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE SalesDetails (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Sale int NOT NULL, 
  Book int NOT NULL,
  Price DECIMAL(12,2) NOT NULL,
  PST DECIMAL(12,2) DEFAULT 0,
  HST DECIMAL(12,2) DEFAULT 0,
  GST DECIMAL(12,2) DEFAULT 0,
  Removed BOOLEAN NOT NULL DEFAULT 0,
  FOREIGN KEY(Sale) REFERENCES Sales(ID) ON DELETE CASCADE,
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Poll (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Selected boolean NOT NULL,
  Question varchar(128) NOT NULL,
  FirstAnswer varchar(128) NOT NULL,
  SecondAnswer varchar(128) NOT NULL,
  ThirdAnswer varchar(128) NOT NULL,
  FourthAnswer varchar(128) NOT NULL,
  FirstCount int DEFAULT 0,
  SecondCount int DEFAULT 0,
  ThirdCount int DEFAULT 0,
  FourthCount int DEFAULT 0
)ENGINE=InnoDB;

CREATE TABLE Admin (
  username varchar(128) PRIMARY KEY,
  Password varchar(254) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE Feed (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(254) NOT NULL UNIQUE,
  URI varchar(254) NOT NULL,
  SELECTED BOOLEAN NOT NULL
)ENGINE=InnoDB;

CREATE TABLE Banner (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  URI varchar(254) NOT NULL,
  SELECTED BOOLEAN NOT NULL
)ENGINE=InnoDB;

CREATE TABLE TaxeRates (
  Province varchar(128) PRIMARY KEY,
  GST DECIMAL(12,2) DEFAULT 0,
  HST DECIMAL(12,2) DEFAULT 0,
  PST DECIMAL(12,2) DEFAULT 0,
  Updated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB;

/* Insert Provinces and Territories of Canada */
INSERT INTO Province (ID) VALUES ("AB");
INSERT INTO Province (ID) VALUES ("BC");
INSERT INTO Province (ID) VALUES ("MB");
INSERT INTO Province (ID) VALUES ("NB");
INSERT INTO Province (ID) VALUES ("NL");
INSERT INTO Province (ID) VALUES ("NT");
INSERT INTO Province (ID) VALUES ("NS");
INSERT INTO Province (ID) VALUES ("NU");
INSERT INTO Province (ID) VALUES ("ON");
INSERT INTO Province (ID) VALUES ("PE");
INSERT INTO Province (ID) VALUES ("QC");
INSERT INTO Province (ID) VALUES ("SK");
INSERT INTO Province (ID) VALUES ("YT");

/* Insert Titles */
INSERT INTO Title (ID) VALUES ("Mr");
INSERT INTO Title (ID) VALUES ("Miss");
INSERT INTO Title (ID) VALUES ("Mrs");
INSERT INTO Title (ID) VALUES ("Mx");
INSERT INTO Title (ID) VALUES ("Honorable");
INSERT INTO Title (ID) VALUES ("Rev");
INSERT INTO Title (ID) VALUES ("Dr");


/* Insert Contribution Types */
INSERT INTO ContributionType (Type) VALUES ("Author");
INSERT INTO ContributionType (Type) VALUES ("Illustrator");
INSERT INTO ContributionType (Type) VALUES ("Colorist");
INSERT INTO ContributionType (Type) VALUES ("Writer");
INSERT INTO ContributionType (Type) VALUES ("Designer");

/* Insert Identifier Types */
INSERT INTO IdentifierType (Type) VALUES ("ISBN-10");
INSERT INTO IdentifierType (Type) VALUES ("ISBN-13");
INSERT INTO IdentifierType (Type) VALUES ("ISNI");

/* Insert Genres */
INSERT INTO Genre (Type) VALUES ("Drama");
INSERT INTO Genre (Type) VALUES ("Graphic Novel");
INSERT INTO Genre (Type) VALUES ("Comedy");
INSERT INTO Genre (Type) VALUES ("Romance");
INSERT INTO Genre (Type) VALUES ("Satire");
INSERT INTO Genre (Type) VALUES ("Tragedy");
INSERT INTO Genre (Type) VALUES ("Horror");
INSERT INTO Genre (Type) VALUES ("Mystery");
INSERT INTO Genre (Type) VALUES ("Western");
INSERT INTO Genre (Type) VALUES ("Fantasy");
INSERT INTO Genre (Type) VALUES ("History");
INSERT INTO Genre (Type) VALUES ("Science Fiction");
INSERT INTO Genre (Type) VALUES ("Cookbook");
INSERT INTO Genre (Type) VALUES ("Travel");
INSERT INTO Genre (Type) VALUES ("Crime");


/* Insert Formats */
INSERT INTO Format (Type) VALUES ("EPUB");
INSERT INTO Format (Type) VALUES ("MOBI");
INSERT INTO Format (Type) VALUES ("PDF");
INSERT INTO Format (Type) VALUES ("PDB");
INSERT INTO Format (Type) VALUES ("AZW");

/* Insert client  */
INSERT INTO client (id, title, lastName, firstName, companyName, homePhoneNumber, cellPhoneNumber, email, password) VALUES
("1", "Mr", "Phillips", "Robin", "Brainsphere", "1-(233)112-8684", "1-(954)156-1204", "rphillips0@phoca.cz", "a"),
("2", "Mrs", "Butler", "Clarence", "Jatri", "1-(788)254-3648", "1-(796)859-1596", "cbutler1@a8.net", "a"),
("3", "Honorable", "Howell", "Keith", "Photobug", "1-(931)728-8172", "1-(597)594-2877", "khowell2@prweb.com", "a"),
("4", "Honorable", "Kim", "Brian", "Thoughtbridge", "1-(186)936-3279", "1-(182)454-9575", "bkim3@baidu.com", "a"),
("5", "Ms", "Watkins", "Willie", "Skyvu", "1-(840)442-8061", "1-(901)658-6313", "wwatkins4@nifty.com", "a"),
("6", "Mr", "Robertson", "Charles", "Mynte", "1-(725)923-7930", "1-(705)730-6521", "crobertson5@fema.gov", "a"),
("7", "Mr", "Thompson", "Adam", "Devify", "1-(663)515-4850", "1-(815)700-6750", "athompson6@pbs.org", "a"),
("8", "Ms", "Long", "Gary", "Yakidoo", "1-(389)419-7534", "1-(265)196-6222", "glong7@engadget.com", "a"),
("9", "Ms", "Hill", "Roy", "Fanoodle", "1-(246)641-0109", "1-(769)794-6315", "rhill8@netvibes.com", "a"),
("10", "Honorable", "Baker", "Carlos", "Browsezoom", "1-(471)224-1870", "1-(901)109-4042", "cbaker9@foxnews.com", "a"),
("11", "Mrs", "Murray", "Bruce", "Zoomcast", "1-(717)579-1167", "1-(326)181-6438", "bmurraya@sina.com.cn", "a"),
("12", "Mr", "Myers", "Nancy", "Voonte", "1-(532)134-7389", "1-(643)203-5756", "nmyersb@dailymotion.com", "a"),
("13", "Mrs", "Gibson", "Carl", "Realpoint", "1-(798)157-3165", "1-(584)610-2609", "cgibsonc@thetimes.co.uk", "a"),
("14", "Rev", "James", "Wayne", "Skipstorm", "1-(143)457-8200", "1-(788)108-9393", "wjamesd@barnesandnoble.com", "a"),
("15", "Mrs", "Hughes", "David", "Voolith", "1-(443)261-4719", "1-(742)392-3243", "dhughese@tuttocitta.it", "a"),
("16", "Dr", "Tucker", "Joseph", "Chatterbridge", "1-(447)715-3597", "1-(570)417-2124", "jtuckerf@unc.edu", "a"),
("17", "Mrs", "Harris", "Fred", "Tekfly", "1-(470)364-6288", "1-(100)614-3424", "fharrisg@netvibes.com", "a"),
("18", "Mr", "Schmidt", "Ashley", "Wikibox", "1-(922)606-6092", "1-(881)194-4963", "aschmidth@yelp.com", "a"),
("19", "Dr", "Wells", "Shirley", "Oloo", "1-(848)148-1690", "1-(935)395-2174", "swellsi@about.com", "a"),
("20", "Ms", "Shaw", "Margaret", "Dynabox", "1-(912)244-9806", "1-(868)667-9200", "mshawj@google.ru", "a"),
("21", "Ms", "Greene", "Samuel", "Jetwire", "1-(394)365-4916", "1-(422)131-3350", "sgreenek@amazon.de", "a"),
("22", "Ms", "Murphy", "Joan", "Trilith", "1-(802)470-7844", "1-(358)566-7161", "jmurphyl@nbcnews.com", "a"),
("23", "Ms", "Lynch", "Harold", "Eayo", "1-(349)734-3413", "1-(780)834-3302", "hlynchm@istockphoto.com", "a"),
("24", "Dr", "Nelson", "Evelyn", "Jatri", "1-(860)904-9198", "1-(882)824-2824", "enelsonn@github.io", "a"),
("25", "Rev", "Gonzalez", "Jimmy", "Yambee", "1-(775)999-1569", "1-(938)386-3138", "jgonzalezo@earthlink.net", "a"),
("26", "Rev", "Williamson", "Johnny", "Izio", "1-(444)823-6043", "1-(331)923-4780", "jwilliamsonp@nps.gov", "a"),
("27", "Dr", "Jacobs", "Irene", "Bluejam", "1-(283)727-8500", "1-(545)730-4880", "ijacobsq@dion.ne.jp", "a"),
("28", "Ms", "Brooks", "Russell", "Jaxnation", "1-(465)185-5905", "1-(241)488-9362", "rbrooksr@weibo.com", "a"),
("29", "Mrs", "Walker", "Bobby", "Gigashots", "1-(279)867-5795", "1-(238)733-1097", "bwalkers@ihg.com", "a"),
("30", "Honorable", "Hunt", "Ruth", "Rhybox", "1-(207)178-4827", "1-(984)268-4666", "rhuntt@delicious.com", "a"),
("31", "Rev", "Jones", "Joan", "Youfeed", "1-(772)303-5697", "1-(566)674-3200", "jjonesu@usa.gov", "a"),
("32", "Honorable", "Hudson", "Benjamin", "Shufflester", "1-(254)663-6818", "1-(648)582-8812", "bhudsonv@walmart.com", "a"),
("33", "Mrs", "Moore", "Justin", "Feednation", "1-(244)801-8915", "1-(210)247-6224", "jmoorew@tripod.com", "a"),
("34", "Ms", "Simmons", "Ruth", "Twitterbridge", "1-(371)908-9368", "1-(983)405-7500", "rsimmonsx@flavors.me", "a"),
("35", "Rev", "Jones", "Donald", "Tagpad", "1-(527)519-4170", "1-(849)884-2265", "djonesy@aol.com", "a"),
("36", "Mr", "Ray", "Joan", "Gigabox", "1-(860)361-6819", "1-(337)850-5137", "jrayz@washington.edu", "a"),
("37", "Honorable", "Hawkins", "Roger", "Ntag", "1-(545)702-7476", "1-(498)703-9801", "rhawkins10@omniture.com", "a"),
("38", "Mr", "Wells", "Lois", "Jamia", "1-(922)732-4129", "1-(813)807-1893", "lwells11@opera.com", "a"),
("39", "Mrs", "Burns", "Shirley", "Yakijo", "1-(192)172-4344", "1-(224)655-2014", "sburns12@newsvine.com", "a"),
("40", "Honorable", "Palmer", "Janice", "Jayo", "1-(609)445-0846", "1-(390)348-1559", "jpalmer13@huffingtonpost.com", "a"),
("41", "Rev", "Cunningham", "Donald", "Bluezoom", "1-(787)480-1861", "1-(972)616-6550", "dcunningham14@vkontakte.ru", "a"),
("42", "Mrs", "Edwards", "Martha", "Mycat", "1-(479)123-7357", "1-(975)912-3915", "medwards15@reverbnation.com", "a"),
("43", "Mrs", "Kim", "Jimmy", "Roomm", "1-(136)935-4035", "1-(868)280-8032", "jkim16@networkadvertising.org", "a"),
("44", "Mr", "Harris", "Stephanie", "Dazzlesphere", "1-(979)928-9966", "1-(173)744-5900", "sharris17@taobao.com", "a"),
("45", "Mr", "Hudson", "David", "Midel", "1-(921)898-6739", "1-(995)403-1302", "dhudson18@harvard.edu", "a"),
("46", "Ms", "Hudson", "Jeffrey", "Yodel", "1-(349)818-5865", "1-(427)597-9074", "jhudson19@cloudflare.com", "a"),
("47", "Dr", "Kennedy", "Edward", "Skinder", "1-(792)363-4415", "1-(601)913-8618", "ekennedy1a@mediafire.com", "a"),
("48", "Mr", "Brown", "Keith", "Feedspan", "1-(412)210-7671", "1-(213)889-0717", "kbrown1b@biblegateway.com", "a"),
("49", "Mrs", "Williamson", "Charles", "Gigazoom", "1-(332)549-0756", "1-(606)397-1605", "cwilliamson1c@jigsy.com", "a"),
("50", "Rev", "Peters", "Brandon", "Wikizz", "1-(992)521-4039", "1-(955)883-9386", "bpeters1d@weibo.com", "a"),
("51", "Rev", "Simpson", "Steve", "Wikizz", "1-(137)270-7490", "1-(728)305-7384", "ssimpson1e@elegantthemes.com", "a"),
("52", "Mrs", "Collins", "Charles", "Dabvine", "1-(719)893-6499", "1-(646)172-1832", "ccollins1f@umich.edu", "a"),
("53", "Rev", "Kim", "Larry", "Bluezoom", "1-(434)836-8897", "1-(818)238-7378", "lkim1g@bizjournals.com", "a"),
("54", "Honorable", "Ramirez", "Janice", "Mycat", "1-(902)814-2348", "1-(797)708-2095", "jramirez1h@youtube.com", "a"),
("55", "Ms", "Banks", "Edward", "Shufflester", "1-(356)210-7646", "1-(203)127-3884", "ebanks1i@dell.com", "a"),
("56", "Mrs", "Martin", "Alice", "Omba", "1-(322)674-5259", "1-(498)976-6238", "amartin1j@cnbc.com", "a"),
("57", "Rev", "Graham", "Judy", "Cogidoo", "1-(106)647-3806", "1-(233)131-9868", "jgraham1k@nps.gov", "a"),
("58", "Mrs", "Gardner", "Nancy", "Riffwire", "1-(662)409-5993", "1-(153)663-3582", "ngardner1l@pen.io", "a"),
("59", "Dr", "Cook", "John", "Photobug", "1-(752)638-8620", "1-(451)756-9927", "jcook1m@illinois.edu", "a"),
("60", "Mrs", "Hughes", "Jeffrey", "Eidel", "1-(747)205-3622", "1-(905)490-2971", "jhughes1n@phoca.cz", "a"),
("61", "Mrs", "Thomas", "Joseph", "Lazzy", "1-(271)346-4225", "1-(432)783-3131", "jthomas1o@ucoz.ru", "a"),
("62", "Dr", "King", "Sharon", "Babbleblab", "1-(672)545-1189", "1-(383)944-1204", "sking1p@umn.edu", "a"),
("63", "Mrs", "Franklin", "Heather", "Zoonoodle", "1-(508)354-0872", "1-(300)346-9452", "hfranklin1q@wufoo.com", "a"),
("64", "Ms", "Bryant", "Thomas", "Janyx", "1-(877)182-1002", "1-(712)653-2741", "tbryant1r@infoseek.co.jp", "a"),
("65", "Dr", "Hunter", "Rebecca", "Yombu", "1-(804)351-5100", "1-(263)868-7832", "rhunter1s@uol.com.br", "a"),
("66", "Rev", "Davis", "Janet", "Npath", "1-(186)552-3181", "1-(476)631-8958", "jdavis1t@biblegateway.com", "a"),
("67", "Mrs", "Wallace", "Amy", "Cogidoo", "1-(423)328-6863", "1-(494)464-1024", "awallace1u@cmu.edu", "a"),
("68", "Honorable", "Murray", "Rebecca", "Yata", "1-(192)224-2125", "1-(949)620-1885", "rmurray1v@aol.com", "a"),
("69", "Rev", "Jenkins", "Karen", "Meeveo", "1-(955)925-1575", "1-(839)848-6381", "kjenkins1w@spotify.com", "a"),
("70", "Ms", "Gomez", "Catherine", "Twinte", "1-(675)258-6523", "1-(719)148-3517", "cgomez1x@unblog.fr", "a"),
("71", "Mr", "Cook", "Irene", "Tazz", "1-(473)939-9124", "1-(479)288-0652", "icook1y@eepurl.com", "a"),
("72", "Mr", "Garza", "Rebecca", "Edgetag", "1-(362)790-4973", "1-(365)113-7709", "rgarza1z@state.tx.us", "a"),
("73", "Mr", "Jordan", "Jerry", "Digitube", "1-(207)536-7893", "1-(184)910-0010", "jjordan20@blog.com", "a"),
("74", "Ms", "Peters", "Randy", "Plajo", "1-(533)857-4860", "1-(641)819-2100", "rpeters21@typepad.com", "a"),
("75", "Rev", "Bryant", "Anthony", "Linklinks", "1-(827)247-4860", "1-(453)186-6165", "abryant22@comsenz.com", "a"),
("76", "Dr", "Ford", "Nicole", "Skipfire", "1-(914)574-9526", "1-(736)685-0858", "nford23@fc2.com", "a"),
("77", "Rev", "Rogers", "Mary", "Jamia", "1-(637)704-8988", "1-(432)872-4519", "mrogers24@yelp.com", "a"),
("78", "Ms", "Mason", "Ralph", "Pixoboo", "1-(343)975-4168", "1-(344)236-2113", "rmason25@europa.eu", "a"),
("79", "Rev", "Roberts", "Craig", "Tagtune", "1-(527)967-7548", "1-(432)543-4161", "croberts26@cbsnews.com", "a"),
("80", "Honorable", "Schmidt", "Mary", "Wikizz", "1-(548)718-0853", "1-(958)501-2480", "mschmidt27@bizjournals.com", "a"),
("81", "Ms", "Martinez", "Harold", "Wikido", "1-(101)229-3150", "1-(534)873-6944", "hmartinez28@hp.com", "a"),
("82", "Dr", "Clark", "Anthony", "Jabbersphere", "1-(436)889-3371", "1-(328)425-3536", "aclark29@jimdo.com", "a"),
("83", "Ms", "Murphy", "Betty", "Mycat", "1-(244)377-6035", "1-(388)220-3136", "bmurphy2a@wsj.com", "a"),
("84", "Honorable", "Wagner", "Annie", "Yozio", "1-(578)615-0954", "1-(415)302-9255", "awagner2b@ed.gov", "a"),
("85", "Rev", "Ortiz", "Donna", "Roombo", "1-(512)140-6393", "1-(543)640-3281", "dortiz2c@oakley.com", "a"),
("86", "Mr", "Coleman", "Theresa", "Yakidoo", "1-(326)906-7616", "1-(931)632-1518", "tcoleman2d@nydailynews.com", "a"),
("87", "Mr", "Mills", "Harry", "Yodoo", "1-(180)735-9249", "1-(697)669-5959", "hmills2e@auda.org.au", "a"),
("88", "Dr", "Green", "Timothy", "Npath", "1-(683)279-9940", "1-(925)481-9123", "tgreen2f@123-reg.co.uk", "a"),
("89", "Mr", "Hawkins", "Jonathan", "Trudoo", "1-(581)298-4558", "1-(209)313-4892", "jhawkins2g@google.nl", "a"),
("90", "Mr", "Webb", "Jesse", "Demivee", "1-(692)484-3159", "1-(877)653-6481", "jwebb2h@discuz.net", "a"),
("91", "Mr", "Peterson", "Edward", "Zooxo", "1-(270)604-2475", "1-(118)408-9996", "epeterson2i@simplemachines.org", "a"),
("92", "Rev", "Kelley", "Joyce", "Fiveclub", "1-(713)802-7390", "1-(802)516-9602", "jkelley2j@1688.com", "a"),
("93", "Honorable", "Harris", "Harry", "Agimba", "1-(322)929-9613", "1-(206)431-3914", "hharris2k@opera.com", "a"),
("94", "Dr", "Meyer", "Lawrence", "Abatz", "1-(141)565-1413", "1-(630)257-4346", "lmeyer2l@wired.com", "a"),
("95", "Dr", "Chapman", "Juan", "Edgepulse", "1-(222)211-7308", "1-(492)461-9198", "jchapman2m@yellowpages.com", "a"),
("96", "Mr", "Nelson", "Ernest", "Livetube", "1-(499)835-7064", "1-(848)609-7334", "enelson2n@purevolume.com", "a"),
("97", "Dr", "Little", "Martha", "Eire", "1-(433)577-8279", "1-(711)675-1843", "mlittle2o@artisteer.com", "a"),
("98", "Rev", "Barnes", "Aaron", "Reallinks", "1-(909)317-3772", "1-(335)587-1209", "abarnes2p@bravesites.com", "a"),
("99", "Rev", "Matthews", "Craig", "Wikido", "1-(587)724-9589", "1-(769)127-7057", "cmatthews2q@webeden.co.uk", "a"),
("100", "Mr", "Perkins", "Jack", "Aimbu", "1-(339)852-3752", "1-(644)280-4203", "jperkins2r@examiner.com", "a");


/* Insert billingaddress  */
INSERT INTO `billingaddress` (`ID`, `Name`, `Client`, `Province`, `City`, `FirstCivicAddress`, `SecondCivicAddress`, `PostalCode`) VALUES
(1, 'Home', 1, 'ON', 'Bancroft', '9169 Elgar Alley', '101', 'h3n5m8'),
(2, '', 2, 'BC', 'Nakusp', '4210 Roth Road', '', 'h3n5m9'),
(3, '', 3, 'BC', 'Coquitlam', '94298 Nevada Circle', '', 'h3n5m1'),
(4, '', 4, 'BC', 'Nakusp', '7518 Ronald Regan Park', '', 'h3n5m1'),
(5, '', 5, 'BC', 'Burns Lake', '631 Holy Cross Drive', '', 'h3n5m1'),
(6, '', 6, 'QC', 'Burgeo', '7 Ramsey Street', '', 'h3n5m1'),
(7, '', 7, 'AB', 'Lamont', '2 Upham Center', '', 'h3n5m1'),
(8, '', 8, 'MB', 'Lac du Bonnet', '78 3rd Hill', '', 'h3n5m1'),
(9, '', 9, 'MB', 'Killarney', '26 Northview Park', '', 'h3n5m1'),
(10, '', 10, 'QC', 'Lachute', '4680 Gale Alley', '', 'h3n5m1'),
(11, '', 11, 'BC', 'Vanderhoof', '2212 Sutherland Court', '', 'h3n5m1'),
(12, '', 12, 'QC', 'East Angus', '53 Bonner Avenue', '', 'h3n5m1'),
(13, '', 13, 'QC', 'La Tuque', '19 Charing Cross Court', '', 'h3n5m1'),
(14, '', 14, 'QC', 'New-Richmond', '61021 Forest Terrace', '', 'h3n5m1'),
(15, '', 15, 'AB', 'Penhold', '8586 Fisk Trail', '', 'h3n5m1'),
(16, '', 16, 'SK', 'Melfort', '93087 Iowa Hill', '', 'h3n5m1'),
(17, '', 17, 'ON', 'Kapuskasing', '365 Warrior Street', '', 'h3n5m1'),
(18, '', 18, 'BC', 'Golden', '3709 Raven Alley', '', 'h3n5m1'),
(19, '', 19, 'ON', 'Fort Erie', '7474 Waubesa Drive', '', 'h3n5m1'),
(20, '', 20, 'AB', 'Killam', '06978 Scott Lane', '', 'h3n5m1'),
(21, '', 21, 'BC', 'Anmore', '587 Rutledge Center', '', 'h3n5m1'),
(22, '', 22, 'ON', 'Skatepark', '694 Haas Street', '', 'h3n5m1'),
(23, '', 23, 'AB', 'Killam', '485 Prairieview Lane', '', 'h3n5m1'),
(24, '', 24, 'QC', 'Saint-Bruno-de-Montarville', '6 Banding Drive', '', 'h3n5m1'),
(25, '', 25, 'ON', 'Markham', '19953 Morningstar Plaza', '', 'h3n5m1'),
(26, '', 26, 'ON', 'St. Thomas', '73533 6th Plaza', '', 'h3n5m1'),
(27, '', 27, 'AB', 'Two Hills', '58 Becker Point', '', 'h3n5m1'),
(28, '', 28, 'BC', 'Golden', '645 Carpenter Avenue', '', 'h3n5m1'),
(29, '', 29, 'AB', 'Crossfield', '1691 Meadow Ridge Park', '', 'h3n5m1'),
(30, '', 30, 'QC', 'Laval', '9193 Heffernan Circle', '', 'h3n5m1'),
(31, '', 31, 'AB', 'Blackfalds', '7 Donald Point', '', 'h3n5m1'),
(32, '', 32, 'SK', 'Martensville', '853 Tomscot Parkway', '', 'h3n5m1'),
(33, '', 33, 'QC', 'Salaberry-de-Valleyfield', '911 Dwight Lane', '', 'h3n5m1'),
(34, '', 34, 'SK', 'Gravelbourg', '84 Gateway Junction', '', 'h3n5m1'),
(35, '', 35, 'QC', 'Saint-Lin-Laurentides', '8882 Tomscot Circle', '', 'h3n5m1'),
(36, '', 36, 'QC', 'Sherbrooke', '08885 Butternut Way', '', 'h3n5m1'),
(37, '', 37, 'AB', 'Claresholm', '3 Golf Court', '', 'h3n5m1'),
(38, '', 38, 'QC', 'Notre-Dame-de-l"ÃŽle-Perrot', '418 Melody Road', '', 'h3n5m1'),
(39, '', 39, 'ON', 'Vaughan', '47966 Warbler Plaza', '', 'h3n5m1'),
(40, '', 40, 'QC', 'Salaberry-de-Valleyfield', '11 Anthes Parkway', '', 'h3n5m1'),
(41, '', 41, 'SK', 'Dalmeny', '14 Sunnyside Alley', '', 'h3n5m1'),
(42, '', 42, 'BC', 'Kamloops', '082 Memorial Trail', '', 'h3n5m1'),
(43, '', 43, 'QC', 'Notre-Dame-des-Prairies', '72 Scofield Court', '', 'h3n5m1'),
(44, '', 44, 'BC', 'Bowen Island', '9 Straubel Center', '', 'h3n5m1'),
(45, '', 45, 'QC', 'Cookshire-Eaton', '04 Holy Cross Junction', '', 'h3n5m1'),
(46, '', 46, 'QC', 'Lacolle', '127 Buhler Terrace', '', 'h3n5m1'),
(47, '', 47, 'QC', 'QC', '9769 Spaight Way', '', 'h3n5m1'),
(48, '', 48, 'AB', 'Vegreville', '2377 Scofield Way', '', 'h3n5m1'),
(49, '', 49, 'QC', 'Mont-Laurier', '06871 6th Center', '', 'h3n5m1'),
(50, '', 50, 'MB', 'Neepawa', '72014 Iowa Park', '', 'h3n5m1'),
(51, '', 51, 'QC', 'Saint-Bruno', '4 Chive Junction', '', 'h3n5m1'),
(52, '', 52, 'QC', 'Napierville', '222 Utah Lane', '', 'h3n5m1'),
(53, '', 53, 'QC', 'Barraute', '81991 Paget Parkway', '', 'h3n5m1'),
(54, '', 54, 'ON', 'Omemee', '192 5th Street', '', 'h3n5m1'),
(55, '', 55, 'ON', 'Bancroft', '80 Bluejay Point', '', 'h3n5m1'),
(56, '', 56, 'ON', 'Thessalon', '47800 Brickson Park Circle', '', 'h3n5m1'),
(57, '', 57, 'QC', 'Barraute', '71640 Pawling Court', '', 'h3n5m1'),
(58, '', 58, 'MB', 'Roblin', '123 Scott Junction', '', 'h3n5m1'),
(59, '', 59, 'SK', 'Rosthern', '7131 Upham Parkway', '', 'h3n5m1'),
(60, '', 60, 'BC', 'Prince Rupert', '590 Commercial Center', '', 'h3n5m1'),
(61, '', 61, 'QC', 'Daveluyville', '82345 Shelley Crossing', '', 'h3n5m1'),
(62, '', 62, 'QC', 'Beaconsfield', '437 Calypso Way', '', 'h3n5m1'),
(63, '', 63, 'AB', 'Raymond', '7 Redwing Street', '', 'h3n5m1'),
(64, '', 64, 'BC', 'Summerland', '876 Old Shore Drive', '', 'h3n5m1'),
(65, '', 65, 'QC', 'Marystown', '55621 Corscot Point', '', 'h3n5m1'),
(66, '', 66, 'ON', 'Renfrew', '8 Stone Corner Junction', '', 'h3n5m1'),
(67, '', 67, 'BC', 'Chase', '09889 Bunting Terrace', '', 'h3n5m1'),
(68, '', 68, 'BC', 'Kimberley', '486 Wayridge Circle', '', 'h3n5m1'),
(69, '', 69, 'NS', 'Lunenburg', '9431 Thompson Court', '', 'h3n5m1'),
(70, '', 70, 'QC', 'Normandin', '91554 Hooker Point', '', 'h3n5m1'),
(71, '', 71, 'SK', 'Swift Current', '97835 Badeau Circle', '', 'h3n5m1'),
(72, '', 72, 'QC', 'Beaconsfield', '1 Lakewood Gardens Circle', '', 'h3n5m1'),
(73, '', 73, 'AB', 'Millet', '2146 Mifflin Junction', '', 'h3n5m1'),
(74, '', 74, 'PE', 'Kensington', '762 Shoshone Way', '', 'h3n5m1'),
(75, '', 75, 'BC', 'Gibsons', '283 2nd Crossing', '', 'h3n5m1'),
(76, '', 76, 'ON', 'Atikokan', '9 Eagan Point', '', 'h3n5m1'),
(77, '', 77, 'ON', 'Petawawa', '7900 Westend Place', '', 'h3n5m1'),
(78, '', 78, 'NT', 'Gjoa Haven', '8739 Laurel Pass', '', 'h3n5m1'),
(79, '', 79, 'ON', 'North Bay', '6042 Bayside Way', '', 'h3n5m1'),
(80, '', 80, 'ON', 'Newmarket', '460 Talmadge Drive', '', 'h3n5m1'),
(81, '', 81, 'MB', 'Boissevain', '6 Harbort Court', '', 'h3n5m1'),
(82, '', 82, 'BC', 'White Rock', '81 Huxley Court', '', 'h3n5m1'),
(83, '', 83, 'ON', 'Arnprior', '372 Columbus Avenue', '', 'h3n5m1'),
(84, '', 84, 'QC', 'Sainte-Martine', '3 Longview Road', '', 'h3n5m1'),
(85, '', 85, 'QC', 'Sainte-Agathe-des-Monts', '0 Rigney Crossing', '', 'h3n5m1'),
(86, '', 86, 'ON', 'Neebing', '33910 Annamark Crossing', '', 'h3n5m1'),
(87, '', 87, 'NB', 'Sussex', '7313 Shelley Street', '', 'h3n5m1'),
(88, '', 88, 'QC', 'Macamic', '2 Boyd Crossing', '', 'h3n5m1'),
(89, '', 89, 'QC', 'Catalina', '220 Corry Hill', '', 'h3n5m1'),
(90, '', 90, 'AB', 'Swan Hills', '374 Chinook Center', '', 'h3n5m1'),
(91, '', 91, 'ON', 'Powassan', '34 Ronald Regan Circle', '', 'h3n5m1'),
(92, '', 92, 'BC', 'Lumby', '1 Logan Drive', '', 'h3n5m1'),
(93, '', 93, 'QC', 'Oka', '41167 Stang Center', '', 'h3n5m1'),
(94, '', 94, 'BC', 'Richmond', '75 Magdeline Crossing', '', 'h3n5m1'),
(95, '', 95, 'BC', 'Lumby', '43 Gina Pass', '', 'h3n5m1'),
(96, '', 96, 'ON', 'Orangeville', '44 Grim Point', '', 'h3n5m1'),
(97, '', 97, 'BC', 'Walnut Grove', '36025 Merry Terrace', '', 'h3n5m1'),
(98, '', 98, 'AB', 'Millet', '54841 Hanover Lane', '', 'h3n5m1'),
(99, '', 99, 'AB', 'Olds', '6847 Buell Circle', '', 'h3n5m1'),
(100, '', 100, 'SK', 'Lanigan', '0701 Scofield Way', '', 'h3n5m1');


/* Insert Books table */
INSERT INTO `Books` (`Title`, `Publisher`, `Pages`, `WholesalePrice`, `ListPrice`, `SalePrice`, `PubDate`, `RemovalStatus`, `Description`) VALUES
('Watchmen', 'DC Comics', 416, '5.85', '12.99', '7.99', '1995-04-01 00:00:00', 0, 'This Hugo Award-winning graphic novel chronicles the fall from grace of a group of super-heroes plagued by all-too-human failings. Along the way, the concept of the super-hero is dissected as the heroes are stalked by an unknown assassin.'),
('V for Vendetta', 'Vertigo', 296, '8.04', '17.87', '11.99', '2008-10-24 00:00:00', 0, 'A frightening and powerful tale of the loss of freedom and identity in a chillingly believable totalitarian world,?V for Vendetta?stands as one of the highest achievements of the comics medium and a defining work for creators Alan Moore and David Lloyd.     \nSet in an imagined future England that has given itself over to fascism, this groundbreaking story captures both the suffocating nature of life in an authoritarian police state and the redemptive power of the human spirit which rebels against it. Crafted with sterling clarity and intelligence, V for Vendetta brings an unequaled depth of characterization and verisimilitude to its unflinching account of oppression and resistance.'),
('Batman: The Dark Knight Returns', 'DC Comics', 197, '8.78', '19.52', '12.99', '1997-05-01 00:00:00', 0, 'Crime runs rampant in the streets, and the man who was Batman is still tortured by the memories of his parents'' murders. As civil society crumbles around him, Bruce Wayne''s long-suppressed vigilante side finally breaks free of its self-imposed shackles. The Dark Knight returns in a blaze of fury, taking on a whole new generation of criminals and matching their level of violence. He is soon joined by this generation''s Robin ? a girl named Carrie Kelley, who proves to be just as invaluable as her predecessors. But can Batman and Robin deal with the threat posed by their deadliest enemies, after years of incarceration have made them into perfect psychopaths? And more important, can anyone survive the coming fallout of an undeclared war between the superpowers - or a clash of what were once the world''s greatest superheroes?'),
('Ghost World', 'Fantagraphics', 80, '2.70', '6.00', '4.39', '2004-06-24 00:00:00', 0, 'Ghost World?has become a cultural and generational touchstone, and continues to enthrall and inspire readers over a decade after its original release as a graphic novel. Originally serialized in the pages of the seminal comic book?Eightball?throughout the mid-1990s, this quasi-autobiographical story (the name of one of the protagonists is famously an anagram of the author''s name) follows the adventures of two teenage girls, Enid and Becky, two best friends facing the prospect of growing up, and more importantly, apart. Daniel Clowes is one of the most respected cartoonists of his generation, and?Ghost World?is his magnum opus. Adapted into a major motion picture directed by Terry Zwigoff (director of the acclaimed documentary?Crumb), which was nominated for an Academy Award for Best Adapted Screenplay. This graphic novel is a must for any self-respecting comics fan''s library'),
('The League of Extraordinary Gentlemen, Vol.1', 'America''s Best Comics', 192, '7.21', '16.02', '8.99', '2011-11-21 00:00:00', 0, 'London, 1898. The Victorian Era draws to a close and the twentieth century approaches. It is a time of great change and an age of stagnation, a period of chaste order and ignoble chaos. It is an era in need of champions. In this amazingly imaginative tale, literary figures from throughout time and various bodies of work are brought together to face any and all threats to Britain. Allan Quatermain, Mina Murray, Captain Nemo, Dr. Henry Jekyll and Edward Hyde and Hawley Griffin ( the Invisible Man) form a remarkable legion of intellectual aptitude and physical prowess: The League of Extraordinary Gentlemen.'),
('Marvel 1602', 'Marvel', 248, '6.19', '13.76', '8.99', '2010-02-24 00:00:00', 0, 'In?Marvel 1602, award-winning writer Neil Gaiman presents a unique vision of the Marvel Universe set four hundred years in the past. Classic Marvel icons such as the X-Men, Spider-Man, the Fantastic Four and Daredevil appear in this intriguing world of 17th-century science and sorcery, instantly familiar to readers, yest subtly different in this new time.?Marvel 1602combines classic Marvel action and adventure with the historically accurate setting of Queen Elizabeth I''s reign to create a unique series unlike any other published by Marvel Comics.'),
('Bone', 'Cartoon Books', 1341, '8.93', '19.84', '15.99', '2005-02-01 00:00:00', 0, 'Three modern cartoon cousins get lost in a pre-technological valley, spending a year there making new friends and out-running dangerous enemies. Their many adventures include crossing the local people in The Great Cow Race, and meeting a giant mountain lion called RockJaw: Master of the Eastern Border. They learn about sacrifice and hardship in The Ghost Circles and finally discover their own true natures in the climatic journey to The Crown of Horns.'),
('Death Note, Vol .1:Boredom', 'VIZ Media LLC', 195, '2.47', '5.49', '4.29', '2011-05-25 00:00:00', 0, 'Light Yagami is an ace student with great prospects - and he''s bored out of his mind. But all that changes when he finds the Death Note, a notebook dropped by a rogue Shinigami, a death god. Any human whose name is written in the notebook dies, and now Light has vowed to use the power of the Death Note to rid the world of evil. But when criminals begin dropping dead, the authorities send the legendary detective L to track down the killer. With L hot on his heels, will Light lose sight of his noble goal...or his life?   Boredom\nLight tests the boundaries of the Death Note''s powers as L and the police begin to close in. Luckily Light''s father is the head of the Japanese National Police Agency and leaves vital information about the case lying around the house. With access to his father''s files, Light can keep one step ahead of the authorities. But who is the strange man following him, and how can Light guard against enemies whose names he doesn''t know?'),
('From Hell', 'Knockabout Comics', 576, '9.84', '21.87', '17.99', '2008-04-18 00:00:00', 0, '"I shall tell you where we are. We''re in the most extreme and utter region of the human mind. A dim, subconscious underworld. A radiant abyss where men meet themselves. Hell, Netley. We''re in Hell." Having proved himself peerless in the arena of reinterpreting superheroes, Alan Moore turned his ever-incisive eye to the squalid, enigmatic world of Jack the Ripper and the Whitechapel murders of 1888. Weighing in at 576 pages,?From Hell?is certainly the most epic of Moore''s works and remarkably and is possibly his finest effort yet in a career punctuated by such glorious highlights as?Watchmen?and?V for Vendetta?. Going beyond the myriad existing theories, which range from the sublime to the ridiculous, Moore presents an ingenious take on the slaughter. His Ripper''s brutal activities are the epicentre of a conspiracy involving the very heart of the British Establishment, including the Freemasons and The Royal Family. A popular claim, which is transformed through Moore''s exquisite and thoroughly gripping vision, of the Ripper crimes being the womb from which the 20th century, so enmeshed in the celebrity culture of violence, received its shocking, visceral birth. Bolstered by meticulous research that encompasses a wide spectrum of Ripper studies and myths and coupled with his ability to evoke sympathies in such monstrous characters, Moore has created perhaps the finest examination of the Ripper legacy, observing far beyond society''s obsessive need to expose Evil''s visage. Ultimately, as Moore observes, Jack''s identity and his actions are inconsequential to the manner in which society embraced the Fear: "It''s about us. It''s about our minds and how they dance. Jack mirrors our hysterias. Faceless, he is the receptacle for each new social panic." Eddie Campbell''s stunning black and white artwork, replete with a scratchy, dirty sheen, is perfectly matched to the often-unshakeable intensity of Moore''s writing. Between them, each murder is rendered in horrifying detail, providing the book''s most unnerving scenes, made more so in uncomfortable, yet lyrical moments as when the villain embraces an eviscerated corpse, craving understanding; pleading that they "are wed in legend, inextricable within eternity". Though technically a comic, the term hardly begins to describe?From Hell''s inimitable grandeur and finesse, as it takes the medium to fresh heights of ingenuity and craftsmanship. Moore and Campbell''s autopsy on the emaciated corpse of the Ripper myth has divulged a deeply disturbing yet undeniably captivating masterpiece. --Danny Graydon'),
('Kingdom Come', 'DC Comics', 231, '2.25', '4.99', '2.99', '2008-09-30 00:00:00', 0, 'Writer Mark Waid, coming from his popular work on Flash and Impulse, and artist Alex Ross, who broke new ground with the beautifully painted Marvels, join together for this explosive book that takes place in a dark alternate future of the DC Superhero Universe. Batman, Superman, Wonder Woman, and almost every other character from DC Comics must choose sides in what could be the final battle of them all.?'),
('300', 'Dark Horse Books', 117, '6.52', '14.49', '9.99', '1999-12-28 00:00:00', 0, 'The army of Persia - a force so vast it shakes the earth with its march - is poised to crush Greece, an island of reason and freedom in a sea of mysticism and tyranny. Standing between Greece and this tidal wave of destruction is a tiny detachment of but three hundred warriors. But these warriors are more than men? they are SPARTANS. 300 is a story of war and defiance as only Frank Miller can tell. Featuring the watercolor talents of painter Lynn Varley, 300 marks the first collaboration for these two creators since 1990''s Elektra Lives Again.'),
('Akira, Vol.1', 'Dark Horse Books', 304, '1.13', '2.50', '1.50', '2014-03-04 00:00:00', 0, 'The science fiction tale set in 2019 in Tokyo after the city was destroyed by World War III, follows the lives of two teenage friends, Tetsuo and Kaneda, who have a consuming fear of a monstrous power known as Akira.'),
('Superman: Red Son', 'DC Comics', 160, '7.18', '15.95', '7.36', '2014-04-08 00:00:00', 0, 'Strange visitor from another world who can change the course of mighty rivers, bend steel in his bare hands ... and who, as the champion of the common worker, fights a never-ending battle for Stalin, Socialism, and the international expansion of the Warsaw Pact. In this Elseworlds tale, a familiar rocketship crash-lands on Earth carrying an infant who will one day become the most powerful being on the planet. But his ship doesn''t land in America. He is not raised in Smallville, Kansas. Instead, he makes his new home on a collective in the Soviet Union!'),
('the Joker', 'DC Comics', 128, '7.98', '17.73', '12.75', '2008-11-04 00:00:00', 0, 'The Joker has been mysteriously released from Arkham Asylum, and he''s none to happy about what''s happened to his Gotham City rackets while he''s been "away." What follows is a harrowing night of revenge, murder and manic crime as only The Joker can deliver it, as he brutally takes back his stolen assets from The Penguin, The Riddler, Two-Face, Killer Croc and others.?'),
('Hellblazer:Dangerous Habits', 'Vertigo?', 160, '6.22', '13.83', '9.99', '2013-05-14 00:00:00', 0, 'John Constantine, the main character in?Hellblazer, was originally a very minor character in DC Comics''?Swamp Thing. Next came his only series, in which this hard-smoking, hard-drinking, all around manipulator walked the thin line of magic between this world and hell. So when Irishman Garth Ennis was asked to write this comic book, he had asked himself, "What could I possibly do to John Constantine that hadn''t been done before? And one course of action suddenly stood out above all others: Kill him." The result is a tense supernatural drama that begins with Constantine being diagnosed with terminal lung cancer. Though this book only hints at the freeform casualness and over-the-top vulgarity that became Ennis''s trademark in the Preacher series, this is an immensely enjoyable read with strong characters and dynamite plot twists.?--Jim Pascoe'),
('Wanted', 'Image Comics', 192, '9.00', '19.99', '14.99', '2007-11-29 00:00:00', 0, 'What if everything in your life was out of your hands and those around you propelled your fate? Your girlfriend left you for your best friend; your boss gave your job to someone better. What if then, after all this, someone gave you back total control? What if he revealed you were the next in line to join a secret society of super-villains that controlled the entire planet? Mark Millar and J.G. Jones provide a look at one man who goes from being the world''s biggest loser to the deadliest assassin alive.'),
('Vagabond, Vol.1', 'VIZ Media LLC', 248, '1.75', '3.89', '2.89', '2008-09-19 00:00:00', 0, 'Shinmen Takezo is destined to become the legendary sword-saint, Miyamoto Musashi--perhaps the most renowned samurai of all time. For now, Takezo is a cold-hearted killer, who will take on anyone in mortal combat to make a name for himself. This is the journey of a wild young brute who strives to reach enlightenment by way of the sword--fighting on the edge of death.'),
('The Ultimates, Vol.1', 'Marvel', 376, '5.85', '12.99', '8.99', '2006-04-12 00:00:00', 0, 'A teenager is climbing walls in Manhattan. Mutants are attacking the White House. Nick Fury, head of the elite espionage agency known as S.H.I.E.L.D., knows the only way to combat these strange new threats is with a team of hisown superhumans. Backed by a billion-dollar budget, Fury recruits Giant Man, the Wasp, Iron Man, Captain America and Thor. And while the team is strong enough to engage in a ferocious battle with the Hulk, will they implode under the weight of their internal conflicts? Rising above their own agendas, the Ultimates forge ahead with the introduction of new allies and face a major global threat. Collects THE ULTIMATES VOL.1: SUPER-HUMAN and THE ULTIMATES VOL. 2: HOMELAND SECURITY. PLUS: A hefty helping of DVD-Style extras!'),
('Ghost in the Shell', 'Dark Horse Comics', 350, '6.68', '14.84', '11.99', '2009-10-13 00:00:00', 0, 'In the rapidly converging landscape of the 21st century Major Kusanagi is charged to track down the craftiest and most dangerous terrorists and cybercriminals, including ghost hackers When he track the trail of one hacker, her quest leads her to a world she could never have imagined.'),
('Hawkeye, Vol.1: My Life as a Weapon', 'Marvel', 136, '5.85', '12.99', '6.99', '2013-08-28 00:00:00', 0, 'The breakout star of this summer''s blockbuster Avengers film, Clint Barton - aka the self-made hero Hawkeye - fights for justice! With ex-Young Avenger Kate Bishop by his side, he''s out to prove himself as one of Earth''s Mightiest Heroes! SHIELD recruits Clint to intercept a packet of incriminating evidence - before he becomes the most wanted man in the world. You won''t believe what is on The Tape! What is the Vagabond Code? Matt Fraction pens a Hawkeye thriller that spans the globe...and the darkest parts of Hawkeye''s mind. Barton and Bishop mean double the Hawkeye and double the trouble...and stealing from the rich never looked so good.'),
('Appalachian Trail Data Book 2016', 'Appalachian Trail Conference', 96, '4.46', '9.91', '9.73', '2016-01-04 00:00:00', 0, 'The Appalachian Trail Data Book 2015 is the thirty-seventh edition: essential for A.T. hiking and planning. A consolidation of the basic information from all 11 A.T. guidebooks into a lightweight table of distances between major Appalachian Trail shelters, road crossings, and features--divided according to the guidebook volumes and updated each fall to account for relocations, new or removed shelters, and other changes. Also keyed to ATC Appalachian Trail maps. Simply an indispensable resource for anyone section hiking on the AT or planning a thruhike.Town locations - Water Sources - Lodging.Road Crossings and supply options.'),
('London Coloring Book', 'AMMO Books', 64, '6.52', '14.49', '14.49', '2016-03-15 00:00:00', 0, 'Joining the enchanting sights of Paris and the high-energy of excitement of New York, AMMO Books presents the newest locale in our popular City Series: stunning and sophisticated London.\nDiscovering historical highlights and opportunities for adventure, readers will embark on a journey through the pages of COME WITH ME TO LONDON. With newfound knowledge of foggy London town, children can play our memory game featuring illustrations of Big Ben and the London Eye and add vibrant color to black-and-white drawings of double-decker buses, the Tower Bridge, and more in a brand new coloring book.'),
('Humans of New York', 'St. Martin''s Press', 432, '15.53', '34.50', '12.23', '2013-10-15 00:00:00', 0, 'Now an instant #1 New York Times bestseller, Humans of New York began in the summer of 2010, when photographer Brandon Stanton set out to create a photographic census of New York City. Armed with his camera, he began crisscrossing the city, covering thousands of miles on foot, all in an attempt to capture New Yorkers and their stories. The result of these efforts was a vibrant blog he called "Humans of New York," in which his photos were featured alongside quotes and anecdotes. The blog has steadily grown, now boasting millions of devoted followers. Humans of New York is the book inspired by the blog. With four hundred color photos, including exclusive portraits and all-new stories, Humans of New York is a stunning collection of images that showcases the outsized personalities of New York. Surprising and moving, printed in a beautiful full-color, hardbound edition, Humans of New York is a celebration of individuality and a tribute to the spirit of the city.'),
('Out of Africa', 'Modern Library', 214, '13.50', '30.00', '22.49', '2013-09-20 00:00:00', 0, 'Out of Africa is Isak Dinesen''s memoir of her years in Africa, from 1914 to 1931, on a four-thousand-acre coffee plantation in the hills near Nairobi. She had come to Kenya from Denmark with her husband, and when they separated she stayed on to manage the farm by herself, visited frequently by her lover, the big-game hunter Denys Finch-Hatton, for whom she would make up stories "like Scheherazade." In Africa, "I learned how to tell tales," she recalled many years later. "The natives have an ear still. I told stories constantly to them, all kinds." Her account of her African adventures, written after she had lost her beloved farm and returned to Denmark, is that of a master storyteller, a woman whom John Updike called "one of the most picturesque and flamboyant literary personalities of the century."'),
('Creative Haven Hello Cuba! Coloring Book', 'Dover Publications', 128, '6.08', '13.50', '13.50', '2016-05-18 00:00:00', 0, 'Sixty-two stunning illustrations pay tribute to?the natural beauty and entrancing culture of?this fascinating tropical island.?Scenes from?town and country include images of vintage cars; tropical wildlife; cigar labels; portraits of fruit vendors, farmers, musicians, and dancers; an array of Cuban fashions; and much more. Pages are perforated and printed on one side only for easy removal and display. Specially designed for experienced colorists, Hello Cuba! and other Creative Haven? coloring books offer an escape to a world of inspiration and artistic fulfillment. Each title is also an effective and fun-filled way to relax and reduce stress.'),
('Japan Book: Comprehensive Pocket Guide', 'Kodansha International', 160, '5.50', '12.23', '12.23', '2004-05-31 00:00:00', 0, 'Distilled from the huge Encyclopedia of Japan and updated in every field it covers, The Japan Book is a pocket almanac: a compilation of brief, useful information on every facet of the country.\n Until its present worldwide publication, this book was used by the Japanese Ministry of Foreign Affairs as a general introduction given to foreign visitors. Now, anyone-whether a businessperson needing facts-at-one''s-fingertips for a quick visit, a diplomat taking up a new post, a tourist wanting basic information without too much detail, or simply the curious reader not knowing much about Japan-can take advantage of this handy manual. Among the subjects covered are: geography, history, government and diplomacy, economy, society, culture, sports and lifestyle. It also contains hundreds of color photos, and comes equipped with maps and charts. The Japan Book is a shortcut to a wealth of useful facts and figures.'),
('Color this Book: New York City', 'Chronicle Books', 32, '6.28', '13.95', '12.29', '2013-04-23 00:00:00', 0, 'Featuring over 30 illustrations by artist and comedienne Abbi Jacobson, this coloring book captures the charm and personality of bustling New York City?from cultural attractions and historic sites to quirky shops and everyday street scenes. A great keepsake for visitors and NY natives of all ages, Color this Book offers hours of coloring fun. Includes Artichoke Pizza, Brooklyn Bridge, Central Park, City Bakery, Greenwich Village, the High Line, the Statue of Liberty, and more!'),
('The Book of Floating: Exploring the Private Sea', 'Gateways Books & Tapes', 274, '15.73', '34.95', '33.19', '2005-01-16 00:00:00', 0, 'A thorough and absorbing summary of the healing and therapeutic uses of the floatation tank invented by Dr. John C. Lilli, the celebrated neuroscience researcher.'),
('Safari: A Photicular Book', 'Workman Publishing Company', 32, '17.53', '38.95', '26.95', '2012-10-02 00:00:00', 0, 'A New York Times bestseller, Safari is a magical journey for the whole family. Readers, as if on African safari, encounter eight wild animals that come alive using never-before-seen Photicular technology. Each full-color image is like a 3-D movie on the page, delivering a rich, fluid, immersive visual experience. The result is breathtaking. The cheetah bounds. The gazelle leaps. The African elephant snaps its ears. The gorilla munches the leaves off a branch. It?s mesmerizing, as visually immediate as a National Geographic or Animal Planet special. Accompanying the images is Safari, the guide: It begins with an evocative journal of a safari along the Mara River in Kenya and interweaves the history of safaris. Then for each animal there is a lively, informative essay and an at-a-glance list of important facts. It?s the romance of being on safari?and the thrill of seeing the animals in motion? in a book unlike any other.'),
('The Magical City: A Colouring Book', 'Michael Joseph', 96, '9.90', '21.99', '19.57', '2015-11-06 00:00:00', 0, 'The Magical City is a brand new colouring book by award-winning illustrator Lizzie Mary Cullen, exploring the hidden magic of cities. Open your mind to the hidden wonder of urban landscapes across the world with this beautifully intricate colouring book. From London to Luxor, follow cobbled pavements through winding streets, look up at skyscrapers soaring to the skies, and gaze over rooftops and dreaming spires. And as you colour and doodle your way through these illustrations, you''ll find hidden details emerge not only on the page but also in the world around you. For fans of Johanna Basford''s The Secret Garden and Millie Marotta''s Animal Kingdom, this is a stunning new colouring book for adults and children alike.'),
('Venice Sketchbook', 'Didier Millet,Csi', 96, '17.55', '39.00', '26.99', '2012-11-16 00:00:00', 0, 'Venice, a mosaic of over 100 islands, many connected by the 400 bridges which span its famous canals, is possibly the most romantic city in the world. It began as a village in the marshes and grew into a formidable sea power, dubbed the Queen of the Adriatic. Now its fading glories - the canals and palaces, monuments and churches - battle with the elements, yet remain breathtakingly beautiful. Artist Fabrice Moireau showcases Venice''s grand attractions and hidden charms through his watercolour paintings and pencil sketches.'),
('Rick Steves'' Spanish Phrase Book & Dictionary', 'Avalon Travel Publishing', 448, '8.53', '18.95', '16.63', '2013-12-03 00:00:00', 0, 'From ordering tapas in Madrid to making new friends in Costa del Sol, it helps to speak some of the native tongue. Rick Steves, bestselling author of travel guides to Europe, offers well-tested phrases and key words to cover every situation a traveler is likely to encounter. This handy guide provides key phrases for use in everyday circumstances, complete with phonetic spelling, an English-Spanish and Spanish-English dictionary, the latest information on European currency and rail transportation, and even a tear-out cheat sheet for continued language practice as you wait in line at the Guggenheim Bilbao. Informative, concise, and practical, Rick Steves'' Spanish Phrase Book and Dictionary is an essential item for any traveler''s mochila.'),
('Paris Coloring Book', 'AMMO Books', 64, '6.52', '14.49', '12.01', '2014-10-01 00:00:00', 0, 'The first in our new city series of children?s titles celebrates everyone?s favorite city in the world: Paris, the city of lights. The hardcover story book, the memory game, and the coloring book are all illustrated in a hand-drawn, colorful, graphic, and vintage style by California-based illustrator Min Heo. All three titles highlight well-loved landmarks such as the Eiffel Tower, Arc de Triomphe, Notre Dame, Sacr?-Couer, the Luxembourg Gardens, the Louvre, and more. They also celebrate important cultural aspects of Paris such as art, architecture, fashion, ballet, and epicurean delights. Pre-schoolers to early readers will enjoy learning a little about French culture and identifying famous Parisian landmarks. This new series is both educational and visually appealing to little kids and design-savvy adults. All three titles together make a perfect gift for would-be travelers of all ages.'),
('Guest Book Vacation: Classic Vacation Guest Book Option', 'CreateSpace Independent Publishing Platform', 106, '4.28', '9.50', '9.33', '2015-10-30 00:00:00', 0, 'The "Classic Vacation Guest Book Edition" was designed with simplicity in mind to cater for that truly, special occasion. The book contains over 400 blank spaces with more than enough room for your guests to leave that unique, personal message. This is something that can be treasured forever, something that you can look back on in future years to help remind you of that wonderful moment in time. PLEASE NOTE: If the color or design of the front cover is not to your taste or does not blend with the occasion in mind, please browse my other "Guest Book" creations to find a more suitable alternative. Yours Sincerely Matthew Harper'),
('Rick Steves'' French Phrase Book & Dictionary', 'Avalon Travel Publishing', 456, '8.53', '18.95', '13.67', '2013-11-26 00:00:00', 0, 'Bonjour! From ordering a caf? au lait in Paris to making new friends in the Loire Valley, it helps to speak some of the native tongue. Rick Steves, bestselling author of travel guides to Europe, offers well-tested phrases and key words to cover every situation a traveler is likely to encounter. This handy guide provides key phrases for use in everyday circumstances and comes complete with phonetic spelling, an English-French and French-English dictionary, the latest information on European currency and rail transportation, and even a tear-out cheat sheet for continued language practice as you wait in line at the Louvre. Informative, concise, and practical, Rick Steves'' French Phrase Book and Dictionary is an essential item for any traveler''s sac ? dos.'),
('Lonely Planet Italian Phrasebook & Dictionary 6th Ed.', 'Lonely Planet', 272, '6.30', '13.99', '11.25', '2015-02-19 00:00:00', 0, 'When even a simple sentence sounds like an aria, it''s difficult to resist striking up a conversation in Italian. Besides, all you need for la dolce vita is to be able to tell your Moschino from your macchiato and your Fellini from your fettuccine ! Get More From Your Trip with Easy-to-Find Phrases for Every Travel Situation! Lonely Planet Phrasebooks have been connecting travellers and locals for over a quarter of a century - our phrasebooks and mobile apps cover more than any other publisher!'),
('A Year in Japan Birthday Book: Dates to Remember Year After Year', 'Chronicle Books', 80, '6.73', '14.95', '5.99', '2008-03-01 00:00:00', 0, 'Charming illustrations of everyday Japanese life make this birthday book a distinctive addition to any desktop.'),
('PEI Book of Musts: 101 Places Every Islander Must Visit', 'MacIntyre Purcell Publishing', 176, '6.28', '13.95', '13.95', '2008-10-01 00:00:00', 0, 'From potters and painters in Breadalbane to a museum devoted exclusively to seaweed in Miminagash, live music at Baba?s Lounge in Charlottetown to a sleep over at the West Point Lighthouse and the wonders of the Daylily farm in Belfast, this is the MUST list every Islander MUST have. From a beach rich with sand dollars and piping plovers, to the best greasy breakfast for your buck, it is all here. We also get Islanders from across the province to give us their MUST lists. They give us the most romantic beaches . . . Tastiest local recipes . . . Weirdest monuments . . . Best places to go antiquing . . . Coolest cafes. This is the ultimate insider MUST list. If you love Prince Edward Island, you simply MUST have the PEI Book of Musts.'),
('The Road to Little Dribbling: More Notes From a Small Island', 'Doubleday Canada', 400, '15.73', '34.95', '25.00', '2015-10-13 00:00:00', 0, 'In 1995, Bill Bryson went on a trip around Britain to celebrate the green and kindly island that had become his home. The hilarious book he wrote about that journey, Notes from a Small Island, became one of the most loved books of recent decades. Now, in this hotly anticipated new travel book, his first in fifteen years and sure to be greeted as the funniest book of the decade, Bryson sets out on a brand-new journey, on a route he dubs the Bryson Line, from Bognor Regis on the south coast to Cape Wrath on the northernmost tip of Scotland. Once again, he will guide us through all that''s best and worst about Britain today--while doing that incredibly rare thing of making us laugh out loud in public.'),
('London Sketchbook', 'Laurence King Publishing', 160, '15.73', '34.95', '25.45', '2014-10-13 00:00:00', 0, 'From the West End to the Square Mile and Harrods to hipster hang-outs, Brooks explores modern-day London through his unique visual repertoire that unites high fashion, fine art, and traveler''s sketches made on the fly. Although best known for his gorgeous fashion illustrations, which feature regularly in Vogue and Elle, travel has been a recurrent theme in Brooks''s work and, with this new volume, his picturesque adventures continue to amuse and inspire. Part guide book, part illustrated journal, this whimsical take on the cosmopolitan city will appeal to both London lovers and fashionistas. Sumptuous production with different stocks and inks will make this a must for anyone who loves fashion illustration and beautiful books.'),
('The Oh She Glows Cookbook', 'Penguin Canada', 336, '13.05', '29.00', '16.99', '2014-03-04 00:00:00', 0, 'A self-trained chef and food photographer, Angela Liddon has spent years perfecting the art of plant-based cooking, creating inventive and delicious recipes that have brought her devoted fans from all over the world. After struggling for a decade with an eating disorder, Angela vowed to change her diet - and her life - once and for all. She traded the low-calorie, processed food she''d been living on for whole, nutrient-packed vegetables, fruits, nuts, whole grains, and more. The result? Her energy soared, she healed her relationship with food, and she got her glow back, both inside and out. Eager to share her realization that the food we put into our bodies has a huge impact on how we look and feel each day, Angela started a blog, Oh She Glows, which is now an internet sensation and one of the most popular vegan recipe blogs on the web. '),
('Do You Know the Muffin Pan', 'Skyhorse Publishing', 240, '11.68', '25.95', '1.99', '2014-11-04 00:00:00', 0, 'Amy Fazio?s debut book, Do You Know the Muffin Pan, is a collection of creative, easy-to-make recipes using that tried-and-true kitchen staple: the muffin pan. No longer just for muffins and cupcakes, the muffin pan is great for creating crowd-pleasing appetizers, delicious side dishes, and even perfectly portion-controlled dinners.This cookbook will showcase over one hundred ways to cook and create in the muffin pan. Many of the recipes include notes on cooking in different-size pans, tips for freezing, and instructions on substituting a variety of ingredients to satisfy even the pickiest eaters.Whether you already love your muffin pan or plan on dusting off an old one, Do You Know the Muffin Pan is sure to become a family favorite.'),
('Daughter of Heaven A Memoir with Earthly Recipes', 'Arcade Publishing', 304, '5.85', '12.99', '1.99', '2011-12-03 00:00:00', 0, 'The powerful yet touching memoir of a Chinese-American woman and her grandmother by an extraordinarily talented author who has been compared to Amy Tan and Maxine Hong Kingston. Leslie Li belongs to the illustrious Li family of Guilin, China. Her paternal grandfather, Li Zongren, was China''s first elected vice president, to whom Chiang Kai-shek left control of the country when he fled to Formosa. Leslie''s father was studying in the US when he met and married her American-born mother.'),
('Thug Kitchen', 'Anansi', 240, '14.38', '31.95', '14.97', '2014-09-15 00:00:00', 0, 'Thug Kitchen started their wildly popular website to inspire people to eatsome goddamn vegetables and adopt a healthier lifestyle. Beloved byGwyneth Paltrow (''This might be my favorite thing ever'') and with half a million Facebook fans and counting, Thug Kitchen wants to show everyone how to take charge of their plates and cook up some real f*cking food. Yeah, plenty of blogs and cookbooks preach about how to eat more kale, whyginger fights inflammation, and how to cook with microgreens andnettles. But they are dull or pretentious as hell -and most people can''t afford the hype. Thug Kitchen lives in the real world. Intheir first cookbook, they''re throwing down more than 100 recipes fortheir best-loved meals, snacks and sides for beginning cooks to homechefs. (Roasted Beer and Lime Cauliflower Tacos? Pumpkin Chili? GrilledPeach Salsa? Believe that sh*t.) Plus they''re going to arm you with allthe info and techniques you need to shop on a budget and go and kick abunch of ass on your own. This book is an invitation toeveryone who wants to do better to elevate their kitchen game. No moreketchup and pizza counting as vegetables. No more drive-thru lines. Nomore avoiding the produce corner of the supermarket. Sh*t is about toget real.'),
('The Clean Eating Cookbook & Diet', 'Rockridge Press', 252, '7.05', '15.67', '0.99', '2013-11-27 00:00:00', 0, 'Clean Eating is a positive lifestyle change that works. The Clean Eating Cookbook & Diet will change the relationship you have with food. Unlike a standard diet that you follow to reach a short-term goal, Clean Eating is a common sense strategy to achieve permanent and lasting good health, without depriving yourself of flavorful food, or feeling guilty after every meal. With a Clean Eating plan, you will understand which foods will be the best fuel your own body, and learn how the right kinds of food will allow you to feel more energetic than ever before. The Clean Eating plan does not require you to eliminate whole food groups or starve yourself. Clean Eating is about a lifetime of enjoying natural, unprocessed foods that taste good and nourish you, paving the way to a stronger, fitter body and mind.'),
('Everyday Super Food', 'HarperCollins Publishers', 352, '17.10', '37.99', '22.79', '2015-10-20 00:00:00', 0, 'This is the most personal book I''ve ever written, and in order to write it I''ve been on a complete journey through the world of health and nutrition. Now, using the thing I know best?incredible food?my wish is that this book will inspire and empower you to live the healthiest, happiest, most productive life you can. Food is there to be enjoyed, shared, and celebrated, and healthy, nourishing food should be colorful, delicious, and fun. This book is full of well-rounded, balanced recipes that will fill you up and tickle your taste buds, and because I''ve done all the hard work on the nutrition front, you can be sure that every choice is a good choice. If you pick up just a handful of ideas from this book, it will change the way you think about food, arming you with the knowledge to get it right on the food front, most of the time.'),
('Inspiralized', 'Clarkson Potter', 224, '8.27', '18.37', '13.99', '2015-02-24 00:00:00', 0, 'On her wildly popular blog, Inspiralized, Ali Maffucci is revolutionizing healthy eating. Whether you?re low-carb, gluten-free, Paleo, or raw, you don?t have to give up the foods you love. Inspiralized shows you how to transform more than 20 vegetables and fruits into delicious meals that look and taste just like your favorite indulgent originals. Zucchini turns into pesto spaghetti; jicama becomes shoestring fries; sweet potatoes lay the foundation for fried rice; plantains transform into ?tortillas? for huevos rancheros. Ali?s recipes for breakfast, snacks, appetizers, sandwiches, soups, salads, casseroles, rices, pastas, and even desserts are easy to follow, hard to mess up, healthful, and completely fresh and flavorful. Best of all, she tells you how to customize them for whatever vegetables you have on hand and whatever your personal goal may be?losing weight, following a healthier lifestyle, or simply making easy meals at home. Here, too, are tons of technical tips and tricks; nutritional information for each dish and every vegetable you can possibly spiralize; and advice for spiralizing whether you?re feeding just yourself, your family, or even a crowd. So bring on a hearty appetite and a sense of adventure?you?re ready to make the most of this secret weapon for healthy cooking.'),
('The Whole30', 'Viking', 432, '15.30', '34.00', '15.99', '2015-04-21 00:00:00', 0, 'Melissa and Dallas Hartwig?s critically-acclaimed Whole30 program has helped hundreds of thousands of people transform how they think about their food, bodies, and lives. Their approach leads to effortless weight loss and better health?along with stunning improvements in sleep quality, energy levels, mood, and self-esteem. Their first book, the New York Times best-selling It Starts With Food, explained the science behind their life-changing program. Now they bring you The Whole30, a stand-alone, step-by-step plan to break unhealthy habits, reduce cravings, improve digestion, and strengthen your immune system. The Whole30 features more than 100 chef-developed recipes, like Chimichurri Beef Kabobs and Halibut with Citrus Ginger Glaze, designed to build your confidence in the kitchen and inspire your taste buds. The book also includes real-life success stories, community resources, and an extensive FAQ to give you the support you need on your journey to ?food freedom.?'),
('Isa Does It', 'Skyhorse Publishing', 176, '16.20', '36.00', '9.99', '2013-10-22 00:00:00', 0, 'How does Isa Chandra Moskowitz make flavorful and satisfying vegan meals from scratch every day, often in 30 minutes or less? It''s easy! In ISA DOES IT, the beloved cookbook author shares 150 new recipes to make weeknight cooking a snap. Mouthwatering recipes like Sweet Potato Red Curry with Rice and Purple Kale, Bistro Beet Burgers, and Summer Seitan Saute with Cilantro and Lime illustrate how simple and satisfying meat-free food can be. The recipes are supermarket friendly and respect how busy most readers are. From skilled vegan chefs, to those new to the vegan pantry, or just cooks looking for some fresh ideas, Isa''s unfussy recipes and quirky commentary will make everyone''s time in the kitchen fun and productive.'),
('Delicious Probiotic Drinks', 'Skyhorse Publishing', 176, '11.68', '25.95', '9.99', '2014-02-04 00:00:00', 0, 'The health benefits of probiotics are no secret?doctors from both the Western and Eastern medicine camps sing the praises of probiotics for their positive effects on digestion, metabolism, and the immune system. Enthusiasts of kombucha?a bubbly probiotic drink now sold regularly in stores from Manhattan delis to Seattle food co-ops?point to its high levels of B vitamins and amino acids, improving mood, energy levels, joint function, ligament health, and skin health. Now you can learn to make kombucha, as well as numerous other probiotic drinks, at home!'),
('The Kitchen Ecosystem', 'Clarkson Potter', 416, '14.40', '32.00', '1.99', '2014-09-30 00:00:00', 0, 'Seasoned cooks know that the secret to great meals is this: the more you cook, the less you actually have to do to produce a delicious meal. The trick is to approach cooking as a continuum, where each meal draws on elements from a previous one and provides the building blocks for another. That synchronicity is a kitchen ecosystem. For the farmers market regular as well as a bulk shopper, for everyday home cooks and aspirational ones, a kitchen ecosystem starts with cooking the freshest in-season ingredients available, preserving some to use in future recipes, and harnessing leftover components for other dishes. In The Kitchen Ecosystem, Eugenia Bone spins multiple dishes from single ingredients: homemade ricotta stars in a pasta dish while the leftover whey is used to braise pork loin; marinated peppers are tossed with shrimp one night and another evening chicken thighs and breast simmer in that leftover marinade. The bones left from a roast chicken bear just enough stock to make stracciatella for two.  The small steps in creating ?supporting ingredients? actually saves time when it comes to putting together dinner. Delicious food is not only a matter exceptional recipes?although there are an abundance of those here. Rather, it is a matter of approaching the kitchen as a system of connected foods. The Kitchen Ecosystem changes the paradigm of how we cook, and in doing so,  it may change everything about the way we eat today.'),
('Sea and Smoke', 'Running Press', 272, '15.30', '33.99', '18.69', '2015-10-27 00:00:00', 0, 'Sea and Smoke is a travelogue chronicling the plucky ambition of a young chef determined to create a world class dining destination in an unlikely place. A native of the Pacific Northwest, two-time James Beard winning chef Blaine Wetzel saw Lummi Island, with fewer than 1,000 residents, as the perfect vehicle for his brand of hyperlocalism: a culinary celebration of what is good and nearby and flavorful. Now, a reservation at The Willows Inn is one of the most sought-after in the world.'),
('Vegan Everyday', 'Robert Rose', 576, '12.49', '27.75', '24.95', '2015-05-15 00:00:00', 0, 'These tempting dishes are bold, innovative, fresh, easy and above all delicious. They reflect this chef''s expertise and complex palate, yet each recipe is both easy and good. Both vegans and non-vegans will find them absolutely delicious.'),
('The Skinnytaste Cookbook', 'Clarkson Potter', 320, '15.75', '35.00', '17.99', '2014-09-30 00:00:00', 0, 'The Skinnytaste Cookbook features 150 amazing recipes: 125 all-new dishes and 25 must-have favorites. As a busy mother of two, Gina started Skinnytaste when she wanted to lose a few pounds herself. She turned to Weight Watchers for help and liked the program but struggled to find enough tempting recipes to help her stay on track. Instead, she started ?skinny-fying? her favorite meals so that she could eat happily while losing weight. With 100 stunning photographs and detailed nutritional information for every recipe, The Skinnytaste Cookbook is an incredible resource of fulfilling, joy-inducing meals that every home cook will love.'),
('Eat to Live', 'Little, Brown and Company', 400, '9.23', '20.50', '7.99', '2011-01-05 00:00:00', 0, 'The Eat To Live 2011 revised edition includes updated scientific research supporting Dr. Fuhrman''s revolutionary six-week plan and a brand new chapter highlighting Dr. Fuhrman''s discovery of toxic hunger and the role of food addiction in weight issues.  This new chapter provides novel and important insights into weight gain. It explains how and why eating the wrong foods causes toxic hunger and the desire to over consume calories; whereas a diet of high micronutrient quality causes true hunger which decreases the sensations leading to food cravings and overeating behaviors.  It instructs readers on how to leave behind the discomfort of toxic hunger, cravings, and addictions to unhealthy foods.'),
('The Food Lab', 'WW Norton', 960, '26.10', '58.00', '20.99', '2015-10-20 00:00:00', 0, 'Ever wondered how to pan-fry a steak with a charred crust and an interior that''s perfectly medium-rare from edge to edge when you cut into it? How to make homemade mac ''n'' cheese that is as satisfyingly gooey and velvety-smooth as the blue box stuff, but far tastier? How to roast a succulent, moist turkey (forget about brining!)?and use a foolproof method that works every time? As Serious Eats''s culinary nerd-in-residence, J. Kenji L?pez-Alt has pondered all these questions and more. In The Food Lab, Kenji focuses on the science behind beloved American dishes, delving into the interactions between heat, energy, and molecules that create great food. Kenji shows that often, conventional methods don?t work that well, and home cooks can achieve far better results using new?but simple?techniques. In hundreds of easy-to-make recipes with over 1,000 full-color images, you will find out how to make foolproof Hollandaise sauce in just two minutes, how to transform one simple tomato sauce into a half dozen dishes, how to make the crispiest, creamiest potato casserole ever conceived, and much more.\nOver 1000 color photographs'),
('Everyday Detox', 'Ten Speed Press', 208, '10.80', '23.99', '13.99', '2015-06-02 00:00:00', 0, 'A healthy guide to detoxing naturally, all year round--no dieting, juice fasting, or calorie counting required--to lose weight, improve digestion, sleep better, and feel great, featuring 100 properly combined recipes for every meal of the day.  Most diets and cleanses have all-or-nothing rules that encourage unhealthy cycles of intense restriction followed by inevitable bingeing. In this healthy guide to detoxing naturally, nutritionist and blogger Megan Gilmore shares 100 delicious, properly combined recipes that will leave you feeling satisfied and well nourished while promoting weight loss and improving digestion and sleep.'),
('Gordon Ramsay''s Home Cooking', 'Grand Central Life & Style', 320, '17.33', '38.50', '12.99', '2013-04-09 00:00:00', 0, 'Based on a new cooking show, this book will give experienced as well as novice cooks the desire, confidence and inspiration to get cooking. Ramsay will offer simple, accessible recipes with a "wow" factor. Gordon has travelled the world from India and the Far East to LA and Europe, and the recipes in this book will draw all these culinary influences together to show us simple, vibrant and delicious recipes that reflect the way we eat today. For example: Miso braised salmon fillet with Asian vegetables, Pork and Bacon slider with home made bbq sauce, Curried Sweetcorn Soup, Wild Mushroom Risotto Arrancini, and Baked Lemon Cheesecake with Raspberries. '),
('Franklin Barbecue', 'Ten Speed Press', 224, '15.75', '35.00', '16.99', '2015-04-07 00:00:00', 0, 'A complete meat- and brisket-cooking education from the country''s most celebrated pitmaster and owner of the wildly popular Austin restaurant Franklin Barbecue--winner of Texas Monthly''s coveted Best Barbecue Joint in Texas award. When Aaron Franklin and his wife, Stacy, opened up a small barbecue trailer on the side of an Austin, Texas, interstate in 2009, they had no idea what they?d gotten themselves into. Today, Franklin Barbecue has grown into the most popular, critically lauded, and obsessed-over barbecue joint in the country (if not the world)?and Franklin is the winner of every major barbecue award there is.'),
('Food52 Genius Recipes', 'Ten Speed Press', 272, '18.45', '41.00', '20.99', '2015-04-07 00:00:00', 0, 'Genius recipes surprise us and make us rethink the way we cook. They might involve an unexpectedly simple technique, debunk a kitchen myth, or apply a familiar ingredient in a new way. They?re handed down by luminaries of the food world and become their legacies. And, once we?ve folded them into our repertoires, they make us feel pretty genius too. In this collection are 100 of the smartest and most remarkable ones.  There isn?t yet a single cookbook where you can find Marcella Hazan?s Tomato Sauce with Onion and Butter, Jim Lahey?s No-Knead Bread, and Nigella Lawson?s Dense Chocolate Loaf Cake?plus dozens more of the most talked about, just-crazy-enough-to-work recipes of our time. Until now.'),
('Ready Player One', 'Broadway Books', 400, '9.45', '21.00', '13.01', '2012-06-05 00:00:00', 0, 'In the year 2044, reality is an ugly place. The only time teenage Wade Watts really feels alive is when he''s jacked into the virtual utopia known as the OASIS. Wade''s devoted his life to studying the puzzles hidden within this world''s digital confines?puzzles that are based on their creator''s obsession with the pop culture of decades past and that promise massive power and fortune to whoever can unlock them. But when Wade stumbles upon the first clue, he finds himself beset by players willing to kill to take this ultimate prize. The race is on, and if Wade''s going to survive, he''ll have to win?and confront the real world he''s always been so desperate to escape.'),
('Leviathan Wakes', 'Orbit', 592, '9.23', '20.50', '18.70', '2011-06-15 00:00:00', 0, 'Two hundred years after migrating into space, mankind is in turmoil. When a reluctant ship''s captain and washed-up detective find themselves involved in the case of a missing girl, what they discover brings our solar system to the brink of civil war, and exposes the greatest conspiracy in human history.'),
('Caliban''s War', 'Orbit', 624, '9.23', '20.50', '16.99', '2012-06-26 00:00:00', 0, 'We are not alone. On Ganymede, breadbasket of the outer planets, a Martian marine watches as her platoon is slaughtered by a monstrous supersoldier. On Earth, a high-level politician struggles to prevent interplanetary war from reigniting. And on Venus, an alien protomolecule has overrun the planet, wreaking massive, mysterious changes and threatening to spread out into the solar system. In the vast wilderness of space, James Holden and the crew of the Rocinante have been keeping the peace for the Outer Planets Alliance. When they agree to help a scientist search war-torn Ganymede for a missing child, the future of humanity rests on whether a single ship can prevent an alien invasion that may have already begun...'),
('Abaddon''s Gate', 'Orbit', 576, '9.23', '20.50', '17.10', '2013-06-04 00:00:00', 0, 'For generations, the solar system -- Mars, the Moon, the Asteroid Belt -- was humanity''s great frontier. Until now. The alien artifact working through its program under the clouds of Venus has appeared in Uranus''s orbit, where it has built a massive gate that leads to a starless dark. Jim Holden and the crew of the Rocinante are part of a vast flotilla of scientific and military ships going out to examine the artifact. But behind the scenes, a complex plot is unfolding, with the destruction of Holden at its core. As the emissaries of the human race try to find whether the gate is an opportunity or a threat, the greatest danger is the one they brought with them.'),
('Mind''s Eye', 'Paragon Press', 360, '8.34', '18.53', '16.99', '2014-01-11 00:00:00', 0, 'Stephen Coonts, 17-time New York Times bestselling author of Pirate Alley When Nick Hall wakes up in a dumpster?bloodied, without a memory, and hearing voices in his head?he knows things are bad. But they''re about to get far worse. Because he?s being hunted by a team of relentless assassins. Soon Hall discovers that advanced electronics have been implanted in his brain, and he now has two astonishing abilities. He can surf the web using thoughts alone. And he can read minds. But who inserted the implants? And why? And why is someone so desperate to kill him? As Hall races to find answers, he comes to learn that far more is at stake than just his life. Because his actions can either catapult civilization to new heights?or bring about its total collapse. Extrapolated from actual research on thought-controlled web surfing, Mind''s Eye is a smart, roller-coaster ride of a thriller. One that raises a number of intriguing, and sometimes chilling, possibilities about a future that is just around the corner. "Recalls the best of Michael Crichton. Intense action, mind-blowing concepts, & breathtaking twists. Enjoy the ride.'),
('The Three-Body Problem', 'Tor Books', 416, '8.33', '18.50', '16.46', '2014-11-11 00:00:00', 0, 'Set against the backdrop of China''s Cultural Revolution, a secret military project sends signals into space to establish contact with aliens. An alien civilization on the brink of destruction captures the signal and plans to invade Earth. Meanwhile, on Earth, different camps start forming, planning to either welcome the superior beings and help them take over a world seen as corrupt, or to fight against the invasion. The result is a science fiction masterpiece of enormous scope and vision.'),
('The Martian', 'Broadway Books', 448, '5.85', '12.99', '10.00', '2014-02-11 00:00:00', 0, 'Six days ago, astronaut Mark Watney became one of the first people to walk on Mars. Now, he''s sure he''ll be the first person to die there. After a dust storm nearly kills him and forces his crew to evacuate while thinking him dead, Mark finds himself stranded and completely alone with no way to even signal Earth that he?s alive?and even if he could get word out, his supplies would be gone long before a rescue could arrive. Chances are, though, he won''t have time to starve to death. The damaged machinery, unforgiving environment, or plain-old "human error" are much more likely to kill him first. But Mark isn''t ready to give up yet. Drawing on his ingenuity, his engineering skills?and a relentless, dogged refusal to quit?he steadfastly confronts one seemingly insurmountable obstacle after the next. Will his resourcefulness be enough to overcome the impossible odds against him?'),
('The Dark Forest', 'Tor Books', 512, '8.33', '18.50', '16.46', '2015-08-11 00:00:00', 0, 'Imagine the universe as a forest, patrolled by numberless and nameless predators. In this forest, stealth is survival - any civilisation that reveals its location is prey. Earth has. And the others are on the way. The Trisolarian fleet has left their homeworld and will arrive...in four centuries'' time. But the sophons, their extra-dimensional emissaries, are already here and have infiltrated human society and and de-railed scientific progress. Only the individual human mind remains immune to the sophons. This is the motivation for the Wallfacer Project, a last-ditch defence that grants four individuals almost absolute power to design secret strategies, hidden through deceit and misdirection from Earth and Trisolaris alike. Three of the Wallfacers are influential statesmen and scientists, but the fourth is a total unknown. Luo Ji, an unambitious Chinese astronomer, is baffled by his new status. All he knows is that he''s the one Wallfacer that Trisolaris wants dead.'),
('Dark Orbit', 'Tor Books', 304, '8.33', '18.50', '16.46', '2015-07-14 00:00:00', 0, 'Reports of a strange, new habitable planet have reached the Twenty Planets of human civilization. When a team of scientists is assembled to investigate this world, exoethnologist Sara Callicot is recruited to keep an eye on an unstable crewmate. Thora was once a member of the interplanetary elite, but since her prophetic delusions helped mobilize a revolt on Orem, she''s been banished to the farthest reaches of space, because of the risk that her very presence could revive unrest. Upon arrival, the team finds an extraordinary crystalline planet, laden with dark matter. Then a crew member is murdered and Thora mysteriously disappears. Thought to be uninhabited, the planet is in fact home to a blind, sentient species whose members navigate their world with a bizarre vocabulary and extrasensory perceptions. Lost in the deep crevasses of the planet among these people, Thora must battle her demons and learn to comprehend the native inhabitants in order to find her crewmates and warn them of an impending danger. But her most difficult task may lie in persuading the crew that some powers lie beyond the boundaries of science.'),
('2312', 'Orbit', 672, '5.40', '12.00', '12.00', '2013-06-25 00:00:00', 0, 'The year is 2312. Scientific and technological advances have opened gateways to an extraordinary future. Earth is no longer humanity''s only home; new habitats have been created throughout the solar system on moons, planets, and in between. But in this year, 2312, a sequence of events will force humanity to confront its past, its present, and its future. The first event takes place on Mercury, on the city of Terminator, itself a miracle of engineering on an unprecedented scale. It is an unexpected death, but one that might have been foreseen. For Swan Er Hong, it is an event that will change her life. Swan was once a woman who designed worlds. Now she will be led into a plot to destroy them.'),
('Redshirts', 'Tor Books', 320, '8.33', '18.50', '13.13', '2013-01-15 00:00:00', 0, 'Ensign Andrew Dahl has just been assigned to the Universal Union Capital Ship Intrepid, flagship of the Universal Union since the year 2456. It''s a prestige posting, with the chance to serve on "Away Missions" alongside the starship''s famous senior officers. Life couldn''t be better...until Andrew begins to realize that 1) every Away Mission involves a lethal confrontation with alien forces, 2) the ship''s senior officers always survive these confrontations, and 3) sadly, at least one low-ranking crew member is invariably killed. Unsurprisingly, the savvier crew members below decks avoid Away Missions at all costs. Then Andrew stumbles on information that transforms his and his colleagues'' understanding of what the starship Intrepid really is...and offers them a crazy, high-risk chance to save their own lives. '),
('Bowl of Heaven', 'Tor Science Fiction', 448, '4.95', '10.99', '10.37', '2013-11-26 00:00:00', 0, 'In Bowl of Heaven, the first collaboration by science fiction masters Larry Niven (Ringworld) and Gregory Benford (Timescape), the limits of wonder are redrawn once again as a human expedition to another star system is jeopardized by an encounter with an astonishingly immense artifact in interstellar space: a bowlshaped structure half-englobing a star, with a habitable area equivalent to many millions of Earths?and it''s on a direct path heading for the same system as the human ship. A landing party is sent to investigate the Bowl, but when the explorers are separated?one group captured by the gigantic structure''s alien inhabitants, the other pursued across its strange and dangerous landscape?the mystery of the Bowl''s origins and purpose propel the human voyagers toward discoveries that will transform their understanding of their place in the universe.'),
('The Goliath Stone', 'Tor Science Fiction', 384, '4.95', '10.99', '9.99', '2013-06-25 00:00:00', 0, 'Doctor Toby Glyer has effected miracle cures with the use of nanotechnology. But Glyer''s controversial nanites are more than just the latest technological advance, they are a new form of life?and they have more uses than just medical. Glyer''s nanites also have the potential to make everyone on Earth rich from the wealth of asteroids. Twenty-five years ago, the Briareus mission took nanomachinery out to divert an Earth-crossing asteroid and bring it back to be mined, only to drop out of contact as soon as it reached its target. The project was shut down and the technology was forcibly suppressed. Now, a much, much larger asteroid is on a collision course with Earth?and the Briareus nanites may be responsible. While the government scrambles to find a solution, Glyer knows that their only hope of avoiding Armageddon lies in the nanites themselves. On the run, Glyer must track down his old partner, William Connors, and find a way to make contact with their wayward children. As every parent learns, when you produce a new thinking being, the plans it makes are not necessarily your plans. But with a two-hundred-gigaton asteroid that rivals the rock that felled the dinosaurs hurtling toward Earth, Glyer and Connors don''t have time to argue. Will Glyer''s nanites be Earth''s salvation or destruction?'),
('Existence', 'Tor Science Fiction', 896, '5.40', '11.99', '11.39', '2012-06-19 00:00:00', 0, 'Gerald Livingston is an orbital garbage collector. For a hundred years, people have been abandoning things in space, and someone has to clean it up. But there''s something spinning a little bit higher than he expects, something that isn''t on the decades'' old orbital maps. An hour after he grabs it and brings it in, rumors fill Earth''s infomesh about an "alien artifact." Thrown into the maelstrom of worldwide shared experience, the Artifact is a game-changer. A message in a bottle; an alien capsule that wants to communicate. The world reacts as humans always do: with fear and hope and selfishness and love and violence. And insatiable curiosity.'),
('Salt Fish Girl', 'Thomas Allen Publishers', 288, '9.88', '21.95', '16.22', '2008-06-17 00:00:00', 0, 'Salt Fish Girl is the mesmerizing tale of an ageless female character who shifts shape and form through time and place. Told in the beguiling voice of a narrator who is fish, snake, girl, and woman - all of whom must struggle against adversity for survival - the novel is set alternately in nineteenth-century China and in a futuristic Pacific Northwest. At turns whimsical and wry, Salt Fish Girl intertwines the story of Nu Wa, the shape-shifter, and that of Miranda, a troubled young girl living in the walled city of Serendipity circa 2044. Miranda is haunted by traces of her mother?s glamourous cabaret career, the strange smell of durian fruit that lingers about her, and odd tokens reminiscient of Nu Wa. Could Miranda be infected by the Dreaming Disease that makes the past leak into the present? Framed by a playful sense of magical realism, Salt Fish Girl reveals a futuristic Pacific Northwest where corporations govern cities, factory workers are cybernetically engineered, middle-class labour is a video game, and those who haven?t sold out to commerce and other ills must fight the evil powers intent on controlling everything. Rich with ancient Chinese mythology and cultural lore, this remarkable novel is about gender, love, honour, intrigue, and fighting against oppression.'),
('High-Rise', 'Fourth Estate', 272, '8.10', '17.99', '16.05', '2012-06-28 00:00:00', 0, 'Within the concealing walls of an elegant forty-storey tower block, the affluent tenants are hell-bent on an orgy of destruction. Cocktail parties degenerate into marauding attacks on ?enemy? floors and the once-luxurious amenities become an arena for riots and technological mayhem. In this visionary tale of urban disillusionment society slips into a violent reverse as the isolated inhabitants of the high-rise, driven by primal urges, create a dystopian world ruled by the laws of the jungle.'),
('Hyperion', 'Spectra', 496, '4.95', '10.99', '10.43', '1990-02-01 00:00:00', 0, 'On the world called Hyperion, beyond the law of the Hegemony of Man, there waits the creature called the Shrike.??There are those who worship it.??There are those who fear it.??And there are those who have vowed to destroy it.??In the Valley of the Time Tombs, where huge, brooding structures move backward through time, the Shrike waits for them all.??On the eve of Armageddon, with the entire galaxy at war, seven pilgrims set forth on a final voyage to Hyperion seeking the answers to the unsolved riddles of their lives.??Each carries a desperate hope--and a terrible secret.??And one may hold the fate of humanity in his hands.'),
('The Left Hand of Darkness', 'Ace', 352, '5.63', '12.50', '11.12', '1987-03-15 00:00:00', 0, 'In Winter, or Gethen, Ursula K. Le Guin has created a fully realized planet and people. But Gethen society is more than merely a fascinating creation. The concept of a society existing totally without sexual prejudices is even more relevant today than it was in 1969. This special 25th anniversary edition of The Left Hand of Darkness contains not only the complete, unaltered text of the landmark original but also a thought-provoking new afterword and four new appendixes by Ms. Le Guin. When the human ambassador Genly Ai is sent to Gethen, the planet known as Winter by those outsiders who have experienced its arctic climate, he thinks that his mission will be a standard one of making peace between warring factions. Instead the ambassador finds himself wildly unprepared. For Gethen is inhabited by a society with a rich, ancient culture full of strange beauty and deadly intrigue?a society of people who are both male and female in one, and neither. This lack of fixed gender, and the resulting lack of gender-based discrimination, is the very cornerstone of Gethen life. But Genly is all too human. Unless he can overcome his ingrained prejudices about the significance of "male" and "female," he may destroy both his mission and himself.'),
('A Canticle for Leibowitz', 'Spectra', 368, '4.50', '9.99', '7.99', '1971-02-02 00:00:00', 0, 'In the depths of the Utah desert, long after the Flame Deluge has scoured the earth clean, a monk of the Order of Saint Leibowitz has made a miraculous discovery: holy relics from the life of the great saint himself, including the blessed blueprint, the sacred shopping list, and the hallowed shrine of the Fallout Shelter. In a terrifying age of darkness and decay, these artifacts could be the keys to mankind''s salvation. But as the mystery at the core of this groundbreaking novel unfolds, it is the search itself?for meaning, for truth, for love?that offers hope for humanity''s rebirth from the ashes.'),
('Second Foundation', 'Spectra', 304, '5.40', '11.99', '9.59', '1991-10-01 00:00:00', 0, 'Isaac Asimov''s Foundation novels are one of the great masterworks of science fiction. As unsurpassed blend of nonstop action, daring ideas, and extensive world-building, they chronicle the struggle of a courageous group of men and women dedicated to preserving humanity''s light in a galaxy plunged into a nightmare of ignorance and violence thirty thousand years long. After years of struggle, the Foundation lies in ruins?destroyed by the mutant mind power of the Mule. But it is rumored that there is a Second Foundation hidden somewhere at the end of the Galaxy, established to preserve the knowledge of mankind through the long centuries of barbarism. The Mule failed to find it the first time?but now he is certain he knows where it lies. The fate of the Foundation rests on young Arcadia Darell, only fourteen years old and burdened with a terrible secret. As its scientists gird for a final showdown with the Mule, the survivors of the First Foundation begin their desperate search. They too want the Second Foundation destroyed?before it destroys them.'),
('An Improbable Friendship', 'Arcade Publishing', 312, '17.55', '38.99', '29.04', '2015-09-15 00:00:00', 0, 'An Improbable Friendship is the dual biography of Israeli Ruth Dayan, now ninety-eight, who was Moshe Dayan?s wife for thirty-seven years, and Palestinian journalist Raymonda Tawil, Yasser Arafat?s mother-in-law, now seventy-four. It reveals for the first time the two women?s surprising and secret forty-year friendship and delivers the story of their extraordinary and turbulent lives growing up in a war-torn country.\nBased on personal interviews, diaries, and journals drawn from both women?Ruth lives today in Tel Aviv, Raymonda in Malta?author Anthony David delivers a fast-paced, fascinating narrative that is a beautiful story of reconciliation and hope in a climate of endless conflict. By experiencing their stories and following their budding relationship, which began after the Six-Day War in 1967, we learn the behind-the-scenes, undisclosed history of the Middle East?s most influential leaders from two prominent women on either side of the ongoing conflict. '),
('Death in the City of Light: The Serial Killer of Nazi-Occupied Paris', 'Crown', 432, '8.55', '19.00', '14.71', '2011-09-20 00:00:00', 0, 'Death in the City of Light is the gripping, true story of a brutal serial killer who unleashed his own reign of terror in Nazi-Occupied Paris. As decapitated heads and dismembered body parts surfaced in the Seine, Commissaire Georges-Victor Massu, head of the Brigade Criminelle, was tasked with tracking down the elusive murderer in a twilight world of Gestapo, gangsters, resistance fighters, pimps, prostitutes, spies, and other shadowy figures of the Parisian underworld.  \nThe main suspect was Dr. Marcel Petiot, a handsome, charming physician with remarkable charisma.  He was the ?People?s Doctor,? known for his many acts of kindness and generosity, not least in providing free medical care for the poor.  Petiot, however, would soon be charged with twenty-seven murders, though authorities suspected the total was considerably higher, perhaps even as many as 150.'),
('The Big Short: Inside The Doomsday Machine', 'WW Norton', 320, '15.75', '35.00', '34.93', '2011-02-01 00:00:00', 0, 'When the crash of the U. S. stock market became public knowledge in the fall of 2008, it was already old news. The real crash, the silent crash, had taken place over the previous year, in bizarre feeder markets where the sun doesn?t shine, and the SEC doesn?t dare, or bother, to tread: the bond and real estate derivative markets where geeks invent impenetrable securities to profit from the misery of lower- and middle-class Americans who can?t pay their debts. The smart people who understood what was or might be happening were paralyzed by hope and fear; in any case, they weren?t talking.'),
('The Art of War', 'Dover Publications', 96, '3.04', '6.75', '5.13', '2002-11-13 00:00:00', 0, 'Widely regarded as "The Oldest Military Treatise in the World," this landmark work covers principles of strategy, tactics, maneuvering, communication, and supplies; the use of terrain, fire, and the seasons of the year; the classification and utilization of spies; the treatment of soldiers, including captives, all have a modern ring to them.'),
('Sapiens: A Brief History of Humankind', 'Signal', 464, '15.73', '34.95', '22.33', '2014-10-28 00:00:00', 0, 'In Sapiens, Dr. Yuval Noah Harari spans the whole of human history, from the very first humans to walk the earth to the radical -- and sometimes devastating -- breakthroughs of the Cognitive, Agricultural, and Scientific Revolutions. Drawing on insights from biology, anthropology, palaeontology, and economics, he explores how the currents of history have shaped our human societies, the animals and plants around us, and even our personalities. Have we become happier as history has unfolded? Can we ever free our behaviour from the heritage of our ancestors? And what, if anything, can we do to influence the course of the centuries to come?\n    Bold, wide-ranging and provocative, Sapiens challenges everything we thought we knew about being human: our thoughts, our actions, our power...and our future.'),
('Humans of New York: Stories', 'St. Martin''s Press', 432, '15.53', '34.50', '25.00', '2015-10-13 00:00:00', 0, 'In the summer of 2010, photographer Brandon Stanton began an ambitious project -to single-handedly create a photographic census of New York City. The photos he took and the accompanying interviews became the blog Humans of New York. His audience steadily grew from a few hundred followers to, at present count, over fifteen million. In 2013, his book Humans of New York, based on that blog, was published and immediately catapulted to the top of the NY Times Bestseller List where it has appeared for over forty-five weeks. Now, Brandon is back with the Humans of New York book that his loyal followers have been waiting for: Humans of New York: Stories. Ever since Brandon began interviewing people on the streets of New York, the dialogue he''s had with them has increasingly become as in-depth, intriguing and moving as the photos themselves. Humans of New York: Stories presents a whole new group of people in stunning photographs, with a rich design and, most importantly, longer stories that delve deeper and surprise with greater candor. Let Brandon Stanton and the Humans of New York he''s photographed astonish you all over again this October.'),
('Man''s Search For Meaning: The Classic Tribute to Hope from the Holocaust', 'Beacon Press', 184, '7.20', '16.00', '14.40', '2011-01-20 00:00:00', 0, 'Psychiatrist Viktor Frankl''s memoir has riveted generations of readers with its descriptions of life in Nazi death camps and its lessons for spiritual survival. Between 1942 and 1945 Frankl labored in four different camps, including Auschwitz, while his parents, brother, and pregnant wife perished. Based on his own experience and the experiences of others he treated later in his practice, Frankl argues that we cannot avoid suffering but we can choose how to cope with it, find meaning in it, and move forward with renewed purpose. Frankl''s theory-known as logotherapy, from the Greek word logos ("meaning")-holds that our primary drive in life is not pleasure, as Freud maintained, but the discovery and pursuit of what we personally find meaningful.'),
('Dark Money: The Hidden History of the Billionaires Behind the Rise of the Radical Right', 'Doubleday', 464, '17.53', '38.95', '34.66', '2016-01-19 00:00:00', 0, 'Why is America living in an age of profound economic inequality? Why, despite the desperate need to address climate change, have even modest environmental efforts been defeated again and again? Why have protections for employees been decimated? Why do hedge-fund billionaires pay a far lower tax rate than middle-class workers?\n     The conventional answer is that a popular uprising against ?big government? led to the ascendancy of a broad-based conservative movement. But as Jane Mayer shows in this powerful, meticulously reported history, a network of exceedingly wealthy people with extreme libertarian views bankrolled a systematic, step-by-step plan to fundamentally alter the American political system.'),
('Magicians of the Gods', 'Coronet', 500, '14.85', '32.99', '24.43', '2015-12-15 00:00:00', 0, 'Graham Hancock''s multi-million bestseller Fingerprints of the Gods remains an astonishing, deeply controversial, wide-ranging investigation of the mysteries of our past and the evidence for Earth''s lost civilization. Twenty years on, Hancock returns with the sequel to his seminal work filled with completely new, scientific and archaeological evidence, which has only recently come to light...\n\nThe evidence revealed in this book shows beyond reasonable doubt that an advanced civilization that flourished during the Ice Age was destroyed in the global cataclysms between 12,800 and 11,600 years ago.'),
('My Secret Sister', 'Pan Macmillan', 448, '7.20', '15.99', '15.00', '2013-03-14 00:00:00', 0, 'The powerful story of two sisters separated at birth, one abused and one loved, and their search to understand their past.\nHelen grew up in a pit village in Tyneside in the post-war years, with her gran, aunties and uncles living nearby. She felt safe with them, but they could not protect her from her neglectful mother and violent father. Behind closed doors, she suffered years of abuse. Sometimes she talked to an imaginary sister, the only one who understood her pain. Jenny was adopted at six weeks and grew up in Newcastle. An only child, she knew she was loved, and with the support of her parents she went on to become a golfing champion, but still she felt that something was missing. . .'),
('13 Hours: The Inside Account of What Really Happened In Benghazi', 'Twelve', 352, '13.95', '31.00', '27.90', '2014-09-09 00:00:00', 0, '13 HOURS presents, for the first time ever, the true account of the events of September 11, 2012, when terrorists attacked the US State Department Special Mission Compound and a nearby CIA station called the Annex in Benghazi, Libya. A team of six American security operators fought to repel the attackers and protect the Americans stationed there. Those men went beyond the call of duty, performing extraordinary acts of courage and heroism, to avert tragedy on a much larger scale. This is their personal account, never before told, of what happened during the thirteen hours of that now-infamous attack.'),
('Unbroken: A World War II Story of Survival, Resilience, and Redemption', 'Random House', 496, '14.85', '33.00', '24.75', '2014-07-29 00:00:00', 0, 'On a May afternoon in 1943, an Army Air Forces bomber crashed into the Pacific Ocean and disappeared, leaving only a spray of debris and a slick of oil, gasoline, and blood. Then, on the ocean surface, a face appeared. It was that of a young lieutenant, the plane?s bombardier, who was struggling to a life raft and pulling himself aboard. So began one of the most extraordinary odysseys of the Second World War.\n\nThe lieutenant?s name was Louis Zamperini. In boyhood, he?d been a cunning and incorrigible delinquent, breaking into houses, brawling, and fleeing his home to ride the rails. As a teenager, he had channeled his defiance into running, discovering a prodigious talent that had carried him to the Berlin Olympics and within sight of the four-minute mile. But when war had come, the athlete had become an airman, embarking on a journey that led to his doomed flight, a tiny raft, and a drift into the unknown.'),
('Meditations', 'White Crow Books', 164, '14.11', '31.36', '30.75', '2003-05-06 00:00:00', 0, 'Meditations'' Marcus Aurelius is considered to one of the great Emperors of Rome who was not only a skilled military leader but also a great philosopher in the Stoic tradition. He was born in 121 AD and both of his parents came from wealthy backgrounds. His father died when Marcus was 3 years old and by the time he was 6 he had gained the attention of the Emperor Hadrian who oversaw his education. Hadrian ensured that Marcus was taught by some of the greatest scholars in Rome who educated him in literature, drama, geometry, Greek oratory, Greek and Latin. Marcus later abandoned most of those subjects in favor of philosophy, with the work of the Greek philosopher Epictetus being a major influence on his thinking. The 12 books that make up ''Meditations'' were not written as an exercise in explaining his philosophy but rather as a personal notebook for self-improvement and study. ''Meditations'' illustrates just how important Epictetus was to Marcus as he quotes the Greek philosopher''s famed ''Discourses'' on more than one occasion.'),
('Red Notice: A True Story of High Finance, Murder, and One Man''s Fight for Justice', 'Simon & Schuster', 416, '9.45', '21.00', '15.00', '2015-02-03 00:00:00', 0, 'A real-life political thriller about an American financier in the Wild East of Russia, the murder of his principled young tax attorney, and his dangerous mission to expose the Kremlin?s corruption.\n\nBill Browder?s journey started on the South Side of Chicago and moved through Stanford Business School to the dog-eat-dog world of hedge fund investing in the 1990s. It continued in Moscow, where Browder made his fortune heading the largest investment fund in Russia after the Soviet Union?s collapse. But when he exposed the corrupt oligarchs who were robbing the companies in which he was investing, Vladimir Putin turned on him and, in 2005, had him expelled from Russia.'),
('Marriage and Civilization: How Monogamy Made Us Human', 'Regnery Publishing', 256, '13.73', '30.50', '25.96', '2014-02-03 00:00:00', 0, 'Marriage built civilization. Will its collapse lead to our downfall?\n\nIn Marriage and Civilization, Tucker takes readers on a journey through the history of the human race to demonstrate how a pattern of life-long, monogamous pairings has enabled humans to build modern civilization. Drawing extensively on biological, anthropological, and historical evidence, Tucker makes the case that marriage is not only a desirable institution for societies, it''s actually the bedrock of civilization.'),
('Spitfire!: The Experiences of a Battle of Britain Fighter Pilot', 'Amberley Publishing', 119, '14.40', '32.00', '29.01', '2015-12-23 00:00:00', 0, 'The remarkable Battle of Britain experiences of Spitfire pilot Brian Lane, DFC. Brian Lane was only 23 when he when he wrote his dramatic account of life as a Spitfire pilot during the Battle of Britain in the summer of 1940. Lane was an ''ace'' with six enemy ''kills'' to his credit and was awarded the DFC for bravery in combat. The text is honest and vibrant, and has the immediacy of a book written close the event, untouched, therefore, by the doubts and debates of later years. Here we can read, exactly what it was like to ''scramble'', to shoot down Messerschmitts, Heinkels, Dorniers and Stukas and how it felt to lose comrades every day. Squadron Leader Brian Lane DFC was not only an exceptional fighter pilot but likewise a gifted leader, at all levels.'),
('Night', 'Hill and Wang', 144, '10.80', '24.00', '18.93', '2006-01-16 00:00:00', 0, 'Night is Elie Wiesel''s masterpiece, a candid, horrific, and deeply poignant autobiographical account of his survival as a teenager in the Nazi death camps. This new translation by Marion Wiesel, Elie''s wife and frequent translator, presents this seminal memoir in the language and spirit truest to the author''s original intent. And in a substantive new preface, Elie reflects on the enduring importance of Night and his lifelong, passionate dedication to ensuring that the world never forgets man''s capacity for inhumanity to man.\n\nNight offers much more than a litany of the daily terrors, everyday perversions, and rampant sadism at Auschwitz and Buchenwald; it also eloquently addresses many of the philosophical as well as personal questions implicit in any serious consideration of what the Holocaust was, what it meant, and what its legacy is and will be.'),
('Dead Wake: The Last Crossing of the Lusitania', 'Crown', 448, '14.63', '32.50', '20.39', '2015-03-10 00:00:00', 0, 'On May 1, 1915, with WWI entering its tenth month, a luxury ocean liner as richly appointed as an English country house sailed out of New York, bound for Liverpool, carrying a record number of children and infants. The passengers were surprisingly at ease, even though Germany had declared the seas around Britain to be a war zone. For months, German U-boats had brought terror to the North Atlantic. But the Lusitania was one of the era?s great transatlantic ?Greyhounds??the fastest liner then in service?and her captain, William Thomas Turner, placed tremendous faith in the gentlemanly strictures of warfare that for a century had kept civilian ships safe from attack.'),
('The Devil in the White City: Murder, Magic, and Madness at the Fair That Changed America', 'Crown', 464, '14.85', '33.00', '31.35', '2004-02-10 00:00:00', 0, 'Two men, each handsome and unusually adept at his chosen work, embodied an element of the great dynamic that characterized America?s rush toward the twentieth century. The architect was Daniel Hudson Burnham, the fair?s brilliant director of works and the builder of many of the country?s most important structures, including the Flatiron Building in New York and Union Station in Washington, D.C. The murderer was Henry H. Holmes, a young doctor who, in a malign parody of the White City, built his ?World?s Fair Hotel? just west of the fairgrounds?a torture palace complete with dissection table, gas chamber, and 3,000-degree crematorium. Burnham overcame tremendous obstacles and tragedies as he organized the talents of Frederick Law Olmsted, Charles McKim, Louis Sullivan, and others to transform swampy Jackson Park into the White City, while Holmes used the attraction of the great fair and his own satanic charms to lure scores of young women to their deaths. What makes the story all the more chilling is that Holmes really lived, walking the grounds of that dream city by the lake.'),
('Between the World and Me', 'Spiegel & Grau', 176, '13.95', '31.00', '27.59', '2015-07-14 00:00:00', 0, 'In a profound work that pivots from the biggest questions about American history and ideals to the most intimate concerns of a father for his son, Ta-Nehisi Coates offers a powerful new framework for understanding our nation?s history and current crisis. Americans have built an empire on the idea of ?race,? a falsehood that damages us all but falls most heavily on the bodies of black women and men?bodies exploited through slavery and segregation, and, today, threatened, locked up, and murdered out of all proportion. What is it like to inhabit a black body and find a way to live within it? And how can we all honestly reckon with this fraught history and free ourselves from its burden?');


/* Insert BookGenres table */
INSERT INTO `BookGenres` (`Book`, `Genre`) VALUES
(1, 'Graphic Novel'),
(2, 'Graphic Novel'),
(3, 'Graphic Novel'),
(4, 'Graphic Novel'),
(5, 'Graphic Novel'),
(6, 'Graphic Novel'),
(7, 'Graphic Novel'),
(8, 'Graphic Novel'),
(9, 'Graphic Novel'),
(10, 'Graphic Novel'),
(11, 'Graphic Novel'),
(12, 'Graphic Novel'),
(13, 'Graphic Novel'),
(14, 'Graphic Novel'),
(15, 'Graphic Novel'),
(16, 'Graphic Novel'),
(17, 'Graphic Novel'),
(18, 'Graphic Novel'),
(19, 'Graphic Novel'),
(20, 'Graphic Novel'),
(21, 'Travel'),
(22, 'Travel'),
(23, 'Travel'),
(24, 'Travel'),
(25, 'Travel'),
(26, 'Travel'),
(27, 'Travel'),
(28, 'Travel'),
(29, 'Travel'),
(30, 'Travel'),
(31, 'Travel'),
(32, 'Travel'),
(33, 'Travel'),
(34, 'Travel'),
(35, 'Travel'),
(36, 'Travel'),
(37, 'Travel'),
(38, 'Travel'),
(39, 'Travel'),
(40, 'Travel'),
(41, 'Cookbook'),
(42, 'Cookbook'),
(43, 'Cookbook'),
(44, 'Cookbook'),
(45, 'Cookbook'),
(46, 'Cookbook'),
(47, 'Cookbook'),
(48, 'Cookbook'),
(49, 'Cookbook'),
(50, 'Cookbook'),
(51, 'Cookbook'),
(52, 'Cookbook'),
(53, 'Cookbook'),
(54, 'Cookbook'),
(55, 'Cookbook'),
(56, 'Cookbook'),
(57, 'Cookbook'),
(58, 'Cookbook'),
(59, 'Cookbook'),
(60, 'Cookbook'),
(61, 'Science Fiction'),
(62, 'Science Fiction'),
(63, 'Science Fiction'),
(64, 'Science Fiction'),
(65, 'Science Fiction'),
(66, 'Science Fiction'),
(67, 'Science Fiction'),
(68, 'Science Fiction'),
(69, 'Science Fiction'),
(70, 'Science Fiction'),
(71, 'Science Fiction'),
(72, 'Science Fiction'),
(73, 'Science Fiction'),
(74, 'Science Fiction'),
(75, 'Science Fiction'),
(76, 'Science Fiction'),
(77, 'Science Fiction'),
(78, 'Science Fiction'),
(79, 'Science Fiction'),
(80, 'Science Fiction'),
(81, 'History'),
(82, 'History'),
(83, 'History'),
(84, 'History'),
(85, 'History'),
(86, 'History'),
(87, 'History'),
(88, 'History'),
(89, 'History'),
(90, 'History'),
(91, 'History'),
(92, 'History'),
(93, 'History'),
(94, 'History'),
(95, 'History'),
(96, 'History'),
(97, 'History'),
(98, 'History'),
(99, 'History'),
(100, 'History');

/* Insert BookIdentifiers table */
INSERT INTO `BookIdentifiers` (`Book`, `Type`, `Code`) VALUES
(1, 'ISBN-13', '978-0930289232'),
(2, 'ISBN-13', '978-1401207922'),
(3, 'ISBN-13', '978-1563893414'),
(4, 'ISBN-13', '978-1560974277'),
(5, 'ISBN-13', '978-1563898587'),
(6, 'ISBN-13', '978-0785123118'),
(7, 'ISBN-13', '978-1888963144'),
(8, 'ISBN-13', '978-1421501680'),
(9, 'ISBN-13', '978-0861661411'),
(10, 'ISBN-13', '978-1563893308'),
(11, 'ISBN-13', '978-1569714027'),
(12, 'ISBN-13', '978-1569714980'),
(13, 'ISBN-13', '978-1401201913'),
(14, 'ISBN-13', '978-1401215811'),
(15, 'ISBN-13', '978-1563891502'),
(16, 'ISBN-13', '978-1582404974'),
(17, 'ISBN-13', '978-1591160342'),
(18, 'ISBN-13', '978-0785110828'),
(19, 'ISBN-13', '978-1593072285'),
(20, 'ISBN-13', '978-0785165620'),
(21, 'ISBN-13', '978-1889386959'),
(22, 'ISBN-13', '978-1623260453'),
(23, 'ISBN-13', '978-1250038821'),
(24, 'ISBN-13', '978-0679600213'),
(25, 'ISBN-13', '978-0486805047'),
(26, 'ISBN-13', '978-4770028471'),
(27, 'ISBN-13', '978-1452117331'),
(28, 'ISBN-13', '978-0895561183'),
(29, 'ISBN-13', '978-0761163800'),
(30, 'ISBN-13', '978-1405924092'),
(31, 'ISBN-13', '978-9814155083'),
(32, 'ISBN-13', '978-1612382050'),
(33, 'ISBN-13', '978-1623260484'),
(34, 'ISBN-13', '978-1518839177'),
(35, 'ISBN-13', '978-1612382029'),
(36, 'ISBN-13', '978-1743214411'),
(37, 'ISBN-13', '978-0811861243'),
(38, 'ISBN-13', '978-0978478414'),
(39, 'ISBN-13', '978-0385685719'),
(40, 'ISBN-13', '978-1780674100'),
(41, 'ISBN-13', '978-0143187226'),
(42, 'ISBN-13', '978-1629146935'),
(43, 'ISBN-13', '978-1559707688'),
(44, 'ISBN-13', '978-1770894655'),
(45, 'ISBN-13', '978-1623152864'),
(46, 'ISBN-13', '978-1443430456'),
(47, 'ISBN-13', '978-0804186834'),
(48, 'ISBN-13', '978-0670069538'),
(49, 'ISBN-13', '978-0316221900'),
(50, 'ISBN-13', '978-1626363922'),
(51, 'ISBN-13', '978-0385345125'),
(52, 'ISBN-13', '978-0762453788'),
(53, 'ISBN-13', '978-0778804994'),
(54, 'ISBN-13', '978-0385345620'),
(55, 'ISBN-13', '978-0316120913'),
(56, 'ISBN-13', '978-0393081084'),
(57, 'ISBN-13', '978-1607747222'),
(58, 'ISBN-13', '978-1455525256'),
(59, 'ISBN-13', '978-1607747208'),
(60, 'ISBN-13', '978-1607747970'),
(61, 'ISBN-13', '978-0307887443'),
(62, 'ISBN-13', '978-0316129084'),
(63, 'ISBN-13', '978-0316129060'),
(64, 'ISBN-13', '978-0316129077'),
(65, 'ISBN-13', '978-0615953946'),
(66, 'ISBN-13', '978-0765382030'),
(67, 'ISBN-13', '978-1101905005'),
(68, 'ISBN-13', '978-0765386690'),
(69, 'ISBN-13', '978-0765336309'),
(70, 'ISBN-13', '978-0316098113'),
(71, 'ISBN-13', '978-0765334794'),
(72, 'ISBN-13', '978-0765366467'),
(73, 'ISBN-13', '978-0765368898'),
(74, 'ISBN-13', '978-0765342621'),
(75, 'ISBN-13', '978-0887623820'),
(76, 'ISBN-13', '978-0586044568'),
(77, 'ISBN-13', '978-0553283686'),
(78, 'ISBN-13', '978-0441478125'),
(79, 'ISBN-13', '978-0553273817'),
(80, 'ISBN-13', '978-0553293364'),
(81, 'ISBN-13', '978-1628725681'),
(82, 'ISBN-13', '978-0307452900'),
(83, 'ISBN-13', '978-0393072235'),
(84, 'ISBN-13', '978-0486425573'),
(85, 'ISBN-13', '978-0771038501'),
(86, 'ISBN-13', '978-1250058904'),
(87, 'ISBN-13', '978-1846042843'),
(88, 'ISBN-13', '978-0385535595'),
(89, 'ISBN-13', '978-1444779677'),
(90, 'ISBN-13', '978-1447228875'),
(91, 'ISBN-13', '978-1455582273'),
(92, 'ISBN-13', '978-1400064168'),
(93, 'ISBN-13', '978-1907661716'),
(94, 'ISBN-13', '978-1476755717'),
(95, 'ISBN-13', '978-1621572015'),
(96, 'ISBN-13', '978-1848683549'),
(97, 'ISBN-13', '978-0374399979'),
(98, 'ISBN-13', '978-0307408860'),
(99, 'ISBN-13', '978-0609608449'),
(100, 'ISBN-13', '978-0812993547');

/* Insert BookFormats table */
INSERT INTO `BookFormats` (`Book`, `Format`, `File`) VALUES
(1, 'EPUB', 'aliceinwonderland.pdf'),
(1, 'MOBI', 'aliceinwonderland.pdf'),
(1, 'PDF', 'aliceinwonderland.pdf'),
(2, 'EPUB', 'aliceinwonderland.pdf'),
(3, 'MOBI', 'aliceinwonderland.pdf'),
(3, 'PDB', 'aliceinwonderland.pdf'),
(3, 'PDF', 'aliceinwonderland.pdf'),
(4, 'EPUB', 'aliceinwonderland.pdf'),
(4, 'MOBI', 'aliceinwonderland.pdf'),
(5, 'EPUB', 'aliceinwonderland.pdf'),
(6, 'PDB', 'aliceinwonderland.pdf'),
(7, 'MOBI', 'aliceinwonderland.pdf'),
(8, 'PDF', 'aliceinwonderland.pdf'),
(9, 'EPUB', 'aliceinwonderland.pdf'),
(9, 'MOBI', 'aliceinwonderland.pdf'),
(9, 'PDB', 'aliceinwonderland.pdf'),
(10, 'MOBI', 'aliceinwonderland.pdf'),
(11, 'MOBI', 'aliceinwonderland.pdf'),
(11, 'PDB', 'aliceinwonderland.pdf'),
(12, 'AZW', 'aliceinwonderland.pdf'),
(13, 'EPUB', 'aliceinwonderland.pdf'),
(13, 'MOBI', 'aliceinwonderland.pdf'),
(13, 'PDF', 'aliceinwonderland.pdf'),
(14, 'MOBI', 'aliceinwonderland.pdf'),
(15, 'MOBI', 'aliceinwonderland.pdf'),
(16, 'EPUB', 'aliceinwonderland.pdf'),
(17, 'AZW', 'aliceinwonderland.pdf'),
(18, 'MOBI', 'aliceinwonderland.pdf'),
(18, 'EPUB', 'aliceinwonderland.pdf'),
(19, 'PDF', 'aliceinwonderland.pdf'),
(20, 'EPUB', 'aliceinwonderland.pdf'),
(21, 'MOBI', 'aliceinwonderland.pdf'),
(22, 'EPUB', 'aliceinwonderland.pdf'),
(22, 'AZW', 'aliceinwonderland.pdf'),
(23, 'EPUB', 'aliceinwonderland.pdf'),
(24, 'EPUB', 'aliceinwonderland.pdf'),
(25, 'EPUB', 'aliceinwonderland.pdf'),
(25, 'MOBI', 'aliceinwonderland.pdf'),
(26, 'PDF', 'aliceinwonderland.pdf'),
(27, 'PDB', 'aliceinwonderland.pdf'),
(28, 'EPUB', 'aliceinwonderland.pdf'),
(29, 'MOBI', 'aliceinwonderland.pdf'),
(30, 'AZW', 'aliceinwonderland.pdf'),
(31, 'PDF', 'aliceinwonderland.pdf'),
(32, 'PDF', 'aliceinwonderland.pdf'),
(33, 'AZW', 'aliceinwonderland.pdf'),
(34, 'EPUB', 'aliceinwonderland.pdf'),
(35, 'MOBI', 'aliceinwonderland.pdf'),
(36, 'EPUB', 'aliceinwonderland.pdf'),
(37, 'EPUB', 'aliceinwonderland.pdf'),
(38, 'MOBI', 'aliceinwonderland.pdf'),
(39, 'EPUB', 'aliceinwonderland.pdf'),
(40, 'EPUB', 'aliceinwonderland.pdf'),
(41, 'EPUB', 'aliceinwonderland.pdf'),
(42, 'MOBI', 'aliceinwonderland.pdf'),
(43, 'PDB', 'aliceinwonderland.pdf'),
(44, 'PDF', 'aliceinwonderland.pdf'),
(45, 'AZW', 'aliceinwonderland.pdf'),
(46, 'EPUB', 'aliceinwonderland.pdf'),
(47, 'MOBI', 'aliceinwonderland.pdf'),
(48, 'AZW', 'aliceinwonderland.pdf'),
(49, 'EPUB', 'aliceinwonderland.pdf'),
(50, 'MOBI', 'aliceinwonderland.pdf'),
(51, 'EPUB', 'aliceinwonderland.pdf'),
(52, 'EPUB', 'aliceinwonderland.pdf'),
(52, 'MOBI', 'aliceinwonderland.pdf'),
(53, 'EPUB', 'aliceinwonderland.pdf'),
(54, 'EPUB', 'aliceinwonderland.pdf'),
(55, 'PDF', 'aliceinwonderland.pdf'),
(55, 'PDB', 'aliceinwonderland.pdf'),
(55, 'AZW', 'aliceinwonderland.pdf'),
(56, 'EPUB', 'aliceinwonderland.pdf'),
(57, 'EPUB', 'aliceinwonderland.pdf'),
(58, 'EPUB', 'aliceinwonderland.pdf'),
(59, 'EPUB', 'aliceinwonderland.pdf'),
(59, 'MOBI', 'aliceinwonderland.pdf'),
(59, 'PDF', 'aliceinwonderland.pdf'),
(60, 'EPUB', 'aliceinwonderland.pdf'),
(60, 'PDB', 'aliceinwonderland.pdf'),
(61, 'EPUB', 'aliceinwonderland.pdf'),
(62, 'EPUB', 'aliceinwonderland.pdf'),
(62, 'MOBI', 'aliceinwonderland.pdf'),
(63, 'AZW', 'aliceinwonderland.pdf'),
(64, 'EPUB', 'aliceinwonderland.pdf'),
(64, 'MOBI', 'aliceinwonderland.pdf'),
(65, 'EPUB', 'aliceinwonderland.pdf'),
(66, 'EPUB', 'aliceinwonderland.pdf'),
(66, 'PDF', 'aliceinwonderland.pdf'),
(67, 'EPUB', 'aliceinwonderland.pdf'),
(68, 'EPUB', 'aliceinwonderland.pdf'),
(69, 'EPUB', 'aliceinwonderland.pdf'),
(70, 'EPUB', 'aliceinwonderland.pdf'),
(71, 'PDB', 'aliceinwonderland.pdf'),
(72, 'EPUB', 'aliceinwonderland.pdf'),
(72, 'AZW', 'aliceinwonderland.pdf'),
(73, 'EPUB', 'aliceinwonderland.pdf'),
(73, 'MOBI', 'aliceinwonderland.pdf'),
(74, 'EPUB', 'aliceinwonderland.pdf'),
(75, 'EPUB', 'aliceinwonderland.pdf'),
(76, 'EPUB', 'aliceinwonderland.pdf'),
(77, 'EPUB', 'aliceinwonderland.pdf'),
(78, 'EPUB', 'aliceinwonderland.pdf'),
(79, 'PDF', 'aliceinwonderland.pdf'),
(80, 'PDB', 'aliceinwonderland.pdf'),
(80, 'AZW', 'aliceinwonderland.pdf'),
(81, 'MOBI', 'aliceinwonderland.pdf'),
(82, 'EPUB', 'aliceinwonderland.pdf'),
(83, 'AZW', 'aliceinwonderland.pdf'),
(84, 'EPUB', 'aliceinwonderland.pdf'),
(85, 'PDF', 'aliceinwonderland.pdf'),
(86, 'EPUB', 'aliceinwonderland.pdf'),
(87, 'AZW', 'aliceinwonderland.pdf'),
(88, 'MOBI', 'aliceinwonderland.pdf'),
(89, 'EPUB', 'aliceinwonderland.pdf'),
(90, 'EPUB', 'aliceinwonderland.pdf'),
(91, 'EPUB', 'aliceinwonderland.pdf'),
(92, 'PDF', 'aliceinwonderland.pdf'),
(93, 'EPUB', 'aliceinwonderland.pdf'),
(94, 'PDB', 'aliceinwonderland.pdf'),
(95, 'MOBI', 'aliceinwonderland.pdf'),
(96, 'EPUB', 'aliceinwonderland.pdf'),
(97, 'EPUB', 'aliceinwonderland.pdf'),
(98, 'EPUB', 'aliceinwonderland.pdf'),
(98, 'PDF', 'aliceinwonderland.pdf'),
(98, 'MOBI', 'aliceinwonderland.pdf'),
(99, 'EPUB', 'aliceinwonderland.pdf'),
(100, 'EPUB', 'aliceinwonderland.pdf'),
(100, 'PDF', 'aliceinwonderland.pdf'),
(100, 'MOBI', 'aliceinwonderland.pdf');

/* Insert Contributor table */
INSERT INTO `Contributor` (`Name`, `Contribution`) VALUES
('Aaron Franklin', 'Author'),
('Abbi Jacobson', 'Author'),
('Alan Moore', 'Author'),
('Ali Maffucci', 'Author'),
('Amy Fazio', 'Author'),
('Andy Weir', 'Author'),
('Angela Liddon', 'Author'),
('Anthony David', 'Author'),
('Bill Bryson ', 'Author'),
('Bill Prowder', 'Author'),
('Blaine Wetzel', 'Author'),
('Brandon Stanton', 'Author'),
('Brian Azzarello', 'Author'),
('Brian Lane', 'Author'),
('Carol Kaufmann ', 'Author'),
('Carolyn Ives Gilman', 'Author'),
('Cixin Liu', 'Author'),
('Dallas Hartwig', 'Author'),
('Dan Kainen', 'Author'),
('Dan Simmons', 'Author'),
('Daniel Chazin ', 'Author'),
('Daniel Clowes', 'Author'),
('David Brin', 'Author'),
('David King', 'Author'),
('Deborah Howard', 'Author'),
('Douglas E. Richards', 'Author'),
('Douglas McNish', 'Author'),
('Eiji Yoshikawa', 'Author'),
('Elie Wiesel', 'Author'),
('Erik Larson', 'Author'),
('Erin Moore', 'Author'),
('Ernest Cline', 'Author'),
('Eugenia Bone', 'Author'),
('Frank Miller', 'Designer'),
('Garth Ennis', 'Author'),
('Gina Homolka', 'Author'),
('Gloria Fowler', 'Author'),
('Gordon Ramsay', 'Author'),
('Graham Hancock', 'Author'),
('Gregory Benfored', 'Author'),
('Helen Edwards', 'Author'),
('Isa Chandra Moskowitz', 'Author'),
('Isaac Asimov', 'Author'),
('Isak Dinesen ', 'Author'),
('J. G. Ballard', 'Author'),
('J. Kenji Lopez-Alt', 'Author'),
('James S.A. Corey', 'Author'),
('Jamie Oliver', 'Author'),
('Jane Mayer', 'Author'),
('Jason Brooks ', 'Author'),
('Jeff Smith', 'Author'),
('Joe Ray', 'Author'),
('Joel Fuhrman', 'Author'),
('John Scalzi', 'Author'),
('Jordan Mackay', 'Author'),
('Julia Mueller', 'Author'),
('Kate T. Williamson', 'Author'),
('Katsuhiro Otomo', 'Author'),
('Kim Stanley Robinson', 'Author'),
('Kodansha International ', 'Author'),
('Kristen Miglore', 'Author'),
('Larissa Lai', 'Author'),
('Larry Niven', 'Author'),
('Yuval Harari', 'Illustrator'),
('Laura Hillenbrand', 'Author'),
('Leslie Li', 'Author'),
('Lizzie Mary Cullen', 'Author'),
('Lonely Planet ', 'Author'),
('Marcus Aurelius', 'Author'),
('Mark Millar', 'Author'),
('Mark Waid', 'Author'),
('Marty Noble', 'Author'),
('Masamune Shirow', 'Author'),
('Matt Fraction', 'Author'),
('Matthew Harper ', 'Author'),
('Matthew Joseph Harrington', 'Author'),
('Megan Gilmore', 'Author'),
('Melissa Hartwig', 'Author'),
('Michael Hutchison', 'Author'),
('Michael Lewis', 'Author'),
('Min Heo', 'Author'),
('MItchell Zuckoff', 'Author'),
('Neil Gaiman', 'Author'),
('Peter Sanderson', 'Author'),
('Rick Steves', 'Author'),
('Rockridge Press', 'Author'),
('Sun Tzu', 'Author'),
('Takehiko Inoue', 'Illustrator'),
('Ta-Nehisi Coates', 'Author'),
('Thug Kitchen Crew', 'Author'),
('Tsugumi Ohba', 'Author'),
('Ursula K. LeGuin', 'Author'),
('Victor E. Frankl', 'Author'),
('Walter Miller', 'Author'),
('William Tucker', 'Author');

/* Insert BookContributors table */
INSERT INTO `BookContributors` (`Book`, `Contributor`) VALUES
(1, 3),
(2, 3),
(3, 35),
(4, 22),
(5, 3),
(6, 83),
(6, 84),
(7, 51),
(8, 91),
(9, 3),
(10, 71),
(11, 35),
(12, 58),
(13, 3),
(14, 13),
(15, 35),
(16, 3),
(17, 88),
(17, 29),
(18, 3),
(19, 73),
(20, 74),
(21, 21),
(22, 37),
(22, 81),
(23, 12),
(24, 44),
(25, 72),
(26, 60),
(27, 2),
(28, 79),
(29, 19),
(29, 15),
(30, 67),
(31, 25),
(32, 85),
(33, 37),
(34, 75),
(35, 85),
(36, 68),
(37, 57),
(38, 31),
(39, 9),
(40, 50),
(41, 7),
(42, 5),
(43, 66),
(44, 90),
(45, 86),
(46, 48),
(47, 4),
(48, 78),
(48, 18),
(49, 42),
(50, 56),
(51, 33),
(52, 11),
(52, 52),
(53, 26),
(54, 36),
(55, 53),
(56, 46),
(57, 77),
(58, 38),
(59, 1),
(59, 55),
(60, 61),
(61, 32),
(62, 47),
(63, 47),
(64, 47),
(65, 26),
(66, 17),
(67, 6),
(68, 17),
(69, 16),
(70, 59),
(71, 54),
(72, 40),
(72, 63),
(73, 63),
(73, 76),
(74, 23),
(75, 62),
(76, 45),
(77, 20),
(78, 92),
(79, 94),
(80, 43),
(81, 8),
(82, 24),
(83, 80),
(84, 87),
(85, 64),
(86, 12),
(87, 93),
(88, 49),
(89, 39),
(90, 41),
(91, 82),
(92, 65),
(93, 69),
(94, 10),
(95, 95),
(96, 14),
(97, 29),
(98, 30),
(99, 30),
(100, 89);


/* Insert reviews  */
INSERT INTO reviews (Book, CreationDate, Client, Rating, Text, Approval) VALUES 
("1", "2015-12-24 00:00:00", "47", "3", "augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus nulla suscipit ligula in lacus curabitur at ipsum ac tellus", "1"),
("2", "2015-12-25 00:00:00", "12", "4", "nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis", "0"),
("3", "2015-12-25 00:00:00", "31", "2", "et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue", "1"),
("4", "2015-12-25 00:00:00", "37", "1", "mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere", "0"),
("5", "2015-12-25 00:00:00", "19", "2", "donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt eu felis fusce posuere felis sed lacus morbi sem", "1"),
("6", "2015-12-25 00:00:00", "95", "2", "ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit", "1"),
("7", "2015-12-25 00:00:00", "84", "4", "turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum ante ipsum primis in", "0"),
("8",  "2015-12-25 00:00:00", "11", "2", "nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis", "0"),
("9",  "2015-12-25 00:00:00", "74", "4", "vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique", "1"),
("10", "2015-12-25 00:00:00",  "34", "3", "adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero", "1"),
("11", "2015-12-25 00:00:00", "82", "4", "rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum", "0"),
("12", "2015-12-25 00:00:00", "32", "4", "eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet", "0"),
("13", "2015-12-25 00:00:00", "44", "1", "donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit amet cursus id turpis integer aliquet massa id lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula", "1"),
("14",  "2015-12-25 00:00:00", "93", "5", "metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam", "1"),
("15", "2015-12-25 00:00:00", "100", "2", "ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti", "0"),
("16", "2015-12-25 00:00:00", "32", "1", "tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse platea dictumst morbi vestibulum velit id pretium iaculis", "0"),
("17",  "2015-12-25 00:00:00", "91", "4", "a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque", "0"),
("18",  "2015-12-25 00:00:00", "44", "3", "amet cursus id turpis integer aliquet massa id lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia", "1"),
("19", "2015-12-25 00:00:00",  "28", "5", "lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu", "0"),
("20","2015-12-25 00:00:00",  "56", "2", "felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque", "1"),
("21", "2015-12-25 00:00:00", "57", "5", "in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar", "0"),
("22", "2015-12-25 00:00:00", "22", "3", "quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent blandit lacinia", "0"),
("23", "2015-12-25 00:00:00", "9", "5", "in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae", "1"),
("24", "2015-12-25 00:00:00", "18", "5", "nulla dapibus dolor vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien", "0"),
("25", "2015-12-25 00:00:00", "12", "3", "tortor quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim", "0"),
("26", "2015-12-25 00:00:00", "84", "4", "lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi", "0"),
("27", "2015-12-25 00:00:00", "97", "5", "molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa", "1"),
("28", "2015-12-25 00:00:00", "55", "1", "neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse", "1"),
("29", "2015-12-25 00:00:00", "95", "4", "consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in", "0"),
("30", "2015-12-25 00:00:00", "49", "2", "maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis", "0"),
("31", "2015-12-25 00:00:00", "76", "1", "vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et", "1"),
("32", "2015-12-25 00:00:00", "6", "5", "diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt eu felis fusce posuere felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus", "0"),
("33", "2015-12-25 00:00:00", "81", "3", "potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem", "1"),
("34", "2015-12-25 00:00:00", "47", "3", "tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id", "1"),
("35", "2015-12-25 00:00:00", "29", "3", "lacinia erat vestibulum sed magna at nunc commodo placerat praesent blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique", "1"),
("36", "2015-12-25 00:00:00", "83", "5", "consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam", "0"),
("37", "2015-12-25 00:00:00", "61", "4", "turpis integer aliquet massa id lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet", "1"),
("38", "2015-12-25 00:00:00", "25", "4", "lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in", "1"),
("39", "2015-12-25 00:00:00", "62", "3", "ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum ante ipsum primis", "1"),
("40", "2015-12-25 00:00:00", "38", "2", "vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi", "1"),
("41", "2015-12-25 00:00:00", "74", "3", "proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit", "0"),
("42", "2015-12-25 00:00:00", "35", "1", "orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit amet cursus id turpis", "0"),
("43", "2015-12-25 00:00:00", "45", "4", "leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus", "0"),
("44", "2015-12-25 00:00:00", "47", "3", "nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus nulla suscipit ligula in lacus curabitur at ipsum ac tellus semper interdum mauris", "0"),
("45", "2015-12-25 00:00:00", "40", "4", "lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam", "1"),
("46", "2015-12-25 00:00:00", "93", "4", "sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor", "1"),
("47", "2015-12-25 00:00:00", "61", "1", "sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue", "1"),
("48", "2015-12-25 00:00:00", "67", "4", "augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus", "1"),
("49", "2015-12-25 00:00:00", "51", "5", "turpis eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis egestas metus", "1"),
("50", "2015-12-25 00:00:00", "58", "2", "convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae", "0"),
("51", "2015-12-25 00:00:00", "25", "4", "suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut", "0"),
("52", "2015-12-25 00:00:00", "97", "5", "odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus nulla suscipit ligula", "1"),
("53", "2015-12-25 00:00:00", "91", "3", "nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis", "1"),
("54", "2015-12-25 00:00:00", "51", "5", "in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit amet cursus id turpis integer aliquet massa id lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh", "0"),
("55", "2015-12-25 00:00:00", "42", "2", "erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet", "1"),
("56", "2015-12-25 00:00:00", "97", "1", "felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit", "1"),
("57", "2015-12-25 00:00:00", "26", "3", "pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse", "1"),
("58", "2015-12-25 00:00:00", "39", "3", "ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at", "1"),
("59", "2015-12-25 00:00:00", "51", "4", "morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula", "0"),
("60", "2015-12-25 00:00:00", "37", "2", "odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis", "0"),
("61", "2015-12-25 00:00:00", "74", "2", "nulla suscipit ligula in lacus curabitur at ipsum ac tellus semper interdum mauris ullamcorper purus sit amet nulla quisque arcu libero rutrum ac lobortis", "0"),
("62", "2015-12-25 00:00:00", "42", "4", "quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus", "0"),
("63", "2015-12-25 00:00:00", "62", "5", "sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi", "0"),
("64", "2015-12-25 00:00:00", "45", "5", "blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi", "1"),
("65", "2015-12-25 00:00:00", "86", "4", "diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae", "1"),
("66", "2015-12-25 00:00:00", "14", "2", "sed magna at nunc commodo placerat praesent blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in", "1"),
("67", "2015-12-25 00:00:00", "59", "4", "dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede", "1"),
("68", "2015-12-25 00:00:00", "22", "1", "proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla", "1"),
("69", "2015-12-25 00:00:00", "82", "1", "phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero", "1"),
("70", "2015-12-25 00:00:00", "46", "5", "sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis", "0"),
("71", "2015-12-25 00:00:00", "37", "4", "penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio", "0"),
("72", "2015-12-25 00:00:00", "31", "5", "sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit", "0"),
("73", "2015-12-25 00:00:00", "22", "5", "cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia", "0"),
("74", "2015-12-25 00:00:00", "28", "3", "luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem", "1"),
("75", "2015-12-25 00:00:00", "13", "5", "in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget", "0"),
("76", "2015-12-25 00:00:00", "55", "4", "feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo etiam", "0"),
("77", "2015-12-25 00:00:00", "95", "1", "nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a", "1"),
("78", "2015-12-25 00:00:00", "71", "1", "aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna", "0"),
("79", "2015-12-25 00:00:00", "49", "2", "quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis", "1"),
("80", "2015-12-25 00:00:00", "76", "3", "eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum", "1"),
("81", "2015-12-25 00:00:00", "42", "4", "molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices", "0"),
("82", "2015-12-25 00:00:00", "47", "4", "sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus", "0"),
("83", "2015-12-25 00:00:00", "25", "1", "donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh", "0"),
("84", "2015-12-25 00:00:00", "38", "2", "et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl", "0"),
("85", "2015-12-25 00:00:00", "53", "3", "nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut", "1"),
("86", "2015-12-25 00:00:00", "5", "4", "duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse platea dictumst morbi vestibulum velit id pretium iaculis diam erat fermentum justo nec condimentum neque sapien placerat ante nulla", "0"),
("87", "2015-12-25 00:00:00", "68", "4", "lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique", "1"),
("88", "2015-12-25 00:00:00", "44", "5", "ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse", "1"),
("89", "2015-12-25 00:00:00", "87", "1", "in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat", "1"),
("90", "2015-12-25 00:00:00", "19", "4", "nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc", "0"),
("91", "2015-12-25 00:00:00", "61", "2", "proin eu mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum", "0"),
("92", "2015-12-25 00:00:00", "52", "3", "dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at", "0"),
("93", "2015-12-25 00:00:00", "30", "5", "nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque", "0"),
("94", "2015-12-25 00:00:00", "71", "3", "bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non", "0"),
("95", "2015-12-25 00:00:00", "86", "2", "ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus", "0"),
("96", "2015-12-25 00:00:00", "94", "4", "faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget", "0"),
("97", "2015-12-25 00:00:00", "89", "1", "rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus", "1"),
("98", "2015-12-25 00:00:00", "29", "1", "tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc commodo placerat praesent blandit nam nulla", "0"),
("99", "2015-12-25 00:00:00", "85", "5", "diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis", "0"),
("100", "2015-12-25 00:00:00", "15", "4", "blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien", "1"),
("1",  "2015-12-25 00:00:00", "44", "2", "hac habitasse platea dictumst morbi vestibulum velit id pretium iaculis diam erat fermentum justo nec condimentum neque sapien placerat ante nulla justo aliquam quis turpis eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis", "1"),
("2",  "2015-12-25 00:00:00", "98", "1", "orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed", "0"),
("3", "2015-12-25 00:00:00", "53", "1", "potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien", "1"),
("4", "2015-12-25 00:00:00", "40", "4", "risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in", "0"),
("5", "2015-12-25 00:00:00", "12", "1", "vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo", "0"),
("6",  "2015-12-25 00:00:00", "48", "4", "neque sapien placerat ante nulla justo aliquam quis turpis eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis", "1"),
("7", "2015-12-25 00:00:00", "23", "2", "posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit amet cursus id turpis integer aliquet massa", "1"),
("8", "2015-12-25 00:00:00", "79", "5", "nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor", "1"),
("9", "2015-12-25 00:00:00", "94", "1", "rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in", "0"),
("10", "2015-12-25 00:00:00",  "47", "3", "cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus", "0"),
("11", "2015-12-25 00:00:00", "38", "3", "leo rhoncus sed vestibulum sit amet cursus id turpis integer aliquet massa id lobortis convallis tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh", "0"),
("12", "2015-12-25 00:00:00", "77", "2", "ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar", "0"),
("13", "2015-12-25 00:00:00",  "99", "5", "non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt eu felis fusce posuere felis sed lacus morbi sem mauris laoreet ut", "0"),
("14", "2015-12-25 00:00:00", "60", "4", "fusce posuere felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat", "1"),
("15", "2015-12-25 00:00:00", "38", "5", "dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices", "0"),
("16", "2015-12-25 00:00:00", "83", "5", "ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam", "1"),
("17", "2015-12-25 00:00:00", "87", "2", "nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia", "1"),
("18",  "2015-12-25 00:00:00", "78", "2", "amet nunc viverra dapibus nulla suscipit ligula in lacus curabitur at ipsum ac tellus semper interdum mauris ullamcorper purus sit", "1"),
("19", "2015-12-25 00:00:00", "43", "3", "aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius", "1"),
("20",  "2015-12-25 00:00:00", "86", "3", "ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non", "0"),
("21",  "2015-12-25 00:00:00", "65", "5", "non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam", "0"),
("22",  "2015-12-25 00:00:00", "85", "1", "mi nulla ac enim in tempor turpis nec euismod scelerisque quam turpis adipiscing lorem vitae mattis nibh ligula nec sem duis aliquam convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in", "0"),
("23", "2015-12-25 00:00:00", "13", "2", "quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan", "1"),
("24", "2015-12-25 00:00:00", "40", "4", "sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui", "1"),
("25", "2015-12-25 00:00:00", "86", "2", "non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac", "0"),
("26", "2015-12-25 00:00:00",  "46", "1", "purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer", "0"),
("27","2015-12-25 00:00:00",  "57", "2", "duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit amet cursus id turpis integer aliquet massa", "1"),
("28", "2015-12-25 00:00:00",  "82", "3", "nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis", "0"),
("29",  "2015-12-25 00:00:00", "13", "3", "curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis", "0"),
("30",  "2015-12-25 00:00:00", "91", "5", "augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc commodo", "1"),
("31",  "2015-12-25 00:00:00", "43", "2", "sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar", "1"),
("32", "2015-12-25 00:00:00", "61", "2", "at lorem integer tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc commodo placerat praesent blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem", "0"),
("33", "2015-12-25 00:00:00", "91", "1", "sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc", "1"),
("34", "2015-12-25 00:00:00", "22", "1", "aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien", "1"),
("35",  "2015-12-25 00:00:00", "36", "1", "a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper", "1"),
("36", "2015-12-25 00:00:00", "2", "4", "non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui", "0"),
("37", "2015-12-25 00:00:00", "46", "3", "adipiscing elit proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et", "1"),
("38",  "2015-12-25 00:00:00", "8", "5", "sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas", "1"),
("39", "2015-12-25 00:00:00", "93", "2", "est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec", "1"),
("40",  "2015-12-25 00:00:00", "72", "5", "vestibulum sed magna at nunc commodo placerat praesent blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula", "0"),
("41", "2015-12-25 00:00:00",  "70", "5", "ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at", "0"),
("42","2015-12-25 00:00:00",  "6", "1", "vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae nulla dapibus dolor vel est donec odio justo sollicitudin", "1"),
("43", "2015-12-25 00:00:00",  "29", "5", "convallis nunc proin at turpis a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in", "0"),
("44",  "2015-12-25 00:00:00", "94", "3", "orci luctus et ultrices posuere cubilia curae nulla dapibus dolor vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien", "0"),
("45",  "2015-12-25 00:00:00", "64", "3", "odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus", "1"),
("46",  "2015-12-25 00:00:00", "59", "5", "porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam orci", "0"),
("47",  "2015-12-25 00:00:00", "1", "3", "sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum", "0"),
("48",  "2015-12-25 00:00:00", "8", "4", "ante nulla justo aliquam quis turpis eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id", "0"),
("49",  "2015-12-25 00:00:00", "38", "1", "in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus", "1"),
("50",  "2015-12-25 00:00:00", "69", "1", "in libero ut massa volutpat convallis morbi odio odio elementum eu interdum eu tincidunt in leo maecenas pulvinar lobortis est phasellus sit amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla", "0"),
("51",  "2015-12-25 00:00:00", "79", "3", "odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien", "1"),
("52",  "2015-12-25 00:00:00", "36", "4", "dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere", "0"),
("53",  "2015-12-25 00:00:00", "38", "5", "in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse platea dictumst morbi vestibulum", "1"),
("54",  "2015-12-25 00:00:00", "13", "2", "lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu", "1"),
("55",  "2015-12-25 00:00:00", "8", "1", "lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed", "0"),
("56", "2015-12-25 00:00:00", "67", "1", "ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia", "1"),
("57",  "2015-12-25 00:00:00", "40", "1", "velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam", "1"),
("58",  "2015-12-25 00:00:00", "87", "2", "ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in", "1"),
("59",  "2015-12-25 00:00:00", "23", "5", "rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa", "0"),
("60", "2015-12-25 00:00:00", "22", "4", "montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia", "0"),
("61", "2015-12-25 00:00:00", "49", "5", "ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean", "1"),
("62",  "2015-12-25 00:00:00", "1", "2", "nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas", "1"),
("63",  "2015-12-25 00:00:00", "40", "1", "potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin", "0"),
("64",  "2015-12-25 00:00:00", "21", "5", "consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam", "1"),
("65",  "2015-12-25 00:00:00", "10", "1", "congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer", "1"),
("66",  "2015-12-25 00:00:00", "78", "5", "maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam", "1"),
("67",  "2015-12-25 00:00:00", "87", "1", "consectetuer adipiscing elit proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in", "0"),
("68", "2015-12-25 00:00:00", "38", "4", "sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut", "0"),
("69", "2015-12-25 00:00:00",  "62", "1", "sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis", "0"),
("70",  "2015-12-25 00:00:00", "6", "5", "aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque", "1"),
("71", "2015-12-25 00:00:00", "24", "3", "in sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat", "1"),
("72", "2015-12-25 00:00:00",  "73", "5", "maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac", "1"),
("73",  "2015-12-25 00:00:00", "40", "5", "iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut massa volutpat convallis morbi odio odio", "0"),
("74",  "2015-12-25 00:00:00", "1", "5", "ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit", "1"),
("75",  "2015-12-25 00:00:00", "11", "1", "nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna", "1"),
("76",  "2015-12-25 00:00:00", "40", "2", "et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo", "1"),
("77",  "2015-12-25 00:00:00", "24", "1", "luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam", "1"),
("78",  "2015-12-25 00:00:00", "9", "3", "orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis", "1"),
("79",  "2015-12-25 00:00:00", "43", "2", "dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus nulla", "1"),
("80",  "2015-12-25 00:00:00", "68", "2", "nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae nulla dapibus dolor vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare", "0"),
("81",  "2015-12-25 00:00:00", "40", "4", "cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl", "0"),
("82",  "2015-12-25 00:00:00", "29", "3", "nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce", "0"),
("83",  "2015-12-25 00:00:00", "68", "4", "ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo rhoncus sed vestibulum sit", "0"),
("84",  "2015-12-25 00:00:00", "49", "2", "diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed", "1"),
("85",  "2015-12-25 00:00:00", "80", "5", "nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus", "0"),
("86",  "2015-12-25 00:00:00", "56", "1", "blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec", "1"),
("87",  "2015-12-25 00:00:00", "43", "3", "odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo", "0"),
("88",  "2015-12-25 00:00:00", "48", "1", "natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet", "1"),
("89",  "2015-12-25 00:00:00", "16", "5", "vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam", "0"),
("90",  "2015-12-25 00:00:00", "40", "1", "sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in", "1"),
("91",  "2015-12-25 00:00:00", "20", "4", "augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel", "1"),
("92", "2015-12-25 00:00:00", "48", "2", "mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit nulla elit ac nulla sed vel enim sit", "0"),
("93",  "2015-12-25 00:00:00", "75", "1", "ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit", "1"),
("94",  "2015-12-25 00:00:00", "35", "1", "semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar nulla pede ullamcorper augue a suscipit", "0"),
("95",  "2015-12-25 00:00:00", "28", "2", "amet erat nulla tempus vivamus in felis eu sapien cursus vestibulum proin eu mi nulla ac enim in tempor turpis nec", "1"),
("96",  "2015-12-25 00:00:00", "57", "2", "volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti", "0"),
("97",  "2015-12-25 00:00:00", "26", "4", "lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis", "0"),
("98",  "2015-12-25 00:00:00", "84", "3", "tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc commodo placerat praesent blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus", "0"),
("99",  "2015-12-25 00:00:00", "77", "4", "rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo", "1"),
("100",  "2015-12-25 00:00:00", "69", "5", "dolor sit amet consectetuer adipiscing elit proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi", "1"),
("1",  "2015-12-25 00:00:00", "20", "5", "nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat in consequat ut nulla sed accumsan felis ut at dolor quis odio consequat varius integer ac leo pellentesque ultrices mattis", "0"),
("2",  "2015-12-25 00:00:00", "68", "2", "sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst etiam faucibus cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus", "1"),
("3",  "2015-12-25 00:00:00", "78", "1", "sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in", "1"),
("4",  "2015-12-25 00:00:00", "64", "2", "est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede", "0"),
("5",  "2015-12-25 00:00:00", "69", "4", "nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus", "1"),
("6",  "2015-12-25 00:00:00", "44", "5", "vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl", "1"),
("7",  "2015-12-25 00:00:00", "7", "4", "rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo", "1"),
("8",  "2015-12-25 00:00:00", "13", "5", "sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula", "1"),
("9",  "2015-12-25 00:00:00", "64", "5", "pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui proin leo odio porttitor id consequat", "0"),
("10", "2015-12-25 00:00:00", "28", "2", "duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue", "0"),
("11", "2015-12-25 00:00:00", "53", "4", "duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec dapibus duis at velit eu est congue", "0"),
("12", "2015-12-25 00:00:00", "54", "3", "aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et", "0"),
("13", "2015-12-25 00:00:00", "38", "2", "faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in", "0"),
("14", "2015-12-25 00:00:00", "62", "1", "quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt", "0"),
("15", "2015-12-25 00:00:00", "57", "3", "lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec nisi volutpat eleifend donec ut dolor morbi vel", "1"),
("16", "2015-12-25 00:00:00", "63", "1", "cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum", "1"),
("17", "2015-12-25 00:00:00", "97", "3", "nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus", "1"),
("18", "2015-12-25 00:00:00", "64", "3", "eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus", "0"),
("19", "2015-12-25 00:00:00", "70", "2", "eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum ante", "0"),
("20", "2015-12-25 00:00:00", "72", "3", "suspendisse ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum felis sed interdum venenatis turpis enim blandit mi in porttitor pede justo eu massa donec", "1"),
("21", "2015-12-25 00:00:00", "56", "4", "iaculis congue vivamus metus arcu adipiscing molestie hendrerit at vulputate vitae nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vehicula condimentum curabitur in libero ut", "0"),
("22", "2015-12-25 00:00:00", "88", "1", "pede justo eu massa donec dapibus duis at velit eu est congue elementum in hac habitasse platea dictumst morbi vestibulum velit id", "0"),
("23", "2015-12-25 00:00:00", "3", "5", "commodo vulputate justo in blandit ultrices enim lorem ipsum dolor sit amet consectetuer adipiscing elit proin interdum mauris non ligula pellentesque ultrices phasellus id sapien in sapien iaculis congue vivamus metus arcu adipiscing molestie", "1"),
("24", "2015-12-25 00:00:00", "68", "3", "auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo", "1"),
("25", "2015-12-25 00:00:00", "38", "2", "suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt eu felis fusce", "0"),
("26", "2015-12-25 00:00:00", "11", "2", "blandit nam nulla integer pede justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat lectus in est risus auctor sed tristique in tempus sit amet sem fusce consequat nulla nisl nunc nisl duis bibendum", "1"),
("27", "2015-12-25 00:00:00", "62", "5", "vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat", "0"),
("28", "2015-12-25 00:00:00", "46", "4", "odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis tristique fusce congue diam id ornare imperdiet sapien urna pretium nisl ut volutpat sapien arcu sed augue aliquam erat volutpat in congue etiam justo etiam pretium iaculis justo in hac habitasse platea dictumst", "1"),
("29", "2015-12-25 00:00:00", "85", "2", "sit amet consectetuer adipiscing elit proin risus praesent lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus accumsan odio curabitur convallis duis consequat dui nec", "1"),
("30", "2015-12-25 00:00:00", "53", "5", "vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin", "1"),
("31", "2015-12-25 00:00:00", "33", "2", "in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent blandit", "1"),
("32", "2015-12-25 00:00:00", "26", "2", "dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum", "1"),
("33", "2015-12-25 00:00:00", "56", "5", "vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in magna bibendum imperdiet", "0"),
("34", "2015-12-25 00:00:00", "6", "2", "et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem praesent id massa id nisl venenatis lacinia aenean sit amet justo morbi ut odio cras mi pede malesuada in imperdiet et commodo vulputate justo in blandit ultrices enim lorem ipsum dolor", "1"),
("35", "2015-12-25 00:00:00", "87", "1", "suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis egestas metus aenean fermentum donec ut mauris eget massa tempor convallis nulla neque libero convallis eget eleifend luctus ultricies eu nibh quisque id justo sit amet sapien dignissim vestibulum vestibulum ante ipsum primis in faucibus orci", "1"),
("36", "2015-12-25 00:00:00", "19", "4", "sem mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta volutpat quam pede lobortis ligula sit amet eleifend pede libero quis orci nullam molestie nibh in lectus pellentesque at nulla suspendisse potenti cras in purus eu magna", "1"),
("37", "2015-12-25 00:00:00", "24", "1", "elementum in hac habitasse platea dictumst morbi vestibulum velit id pretium iaculis diam erat fermentum justo nec condimentum neque sapien placerat ante nulla justo aliquam quis turpis eget elit sodales scelerisque mauris sit amet eros suspendisse accumsan tortor quis turpis sed ante vivamus tortor duis mattis", "0"),
("38", "2015-12-25 00:00:00", "84", "2", "montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus etiam vel augue vestibulum rutrum rutrum neque aenean auctor gravida sem", "1"),
("39", "2015-12-25 00:00:00", "69", "4", "magna ac consequat metus sapien ut nunc vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae mauris viverra diam vitae quam suspendisse potenti nullam porttitor lacus at turpis donec posuere metus vitae ipsum aliquam non mauris morbi non lectus aliquam sit amet diam in", "1"),
("40", "2015-12-25 00:00:00", "53", "4", "diam in magna bibendum imperdiet nullam orci pede venenatis non sodales sed tincidunt eu felis fusce posuere felis sed lacus morbi sem mauris laoreet ut rhoncus aliquet pulvinar sed nisl nunc rhoncus dui vel sem sed sagittis nam congue risus semper porta", "1"),
("41", "2015-12-25 00:00:00", "62", "5", "in faucibus orci luctus et ultrices posuere cubilia curae nulla dapibus dolor vel est donec odio justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est lacinia nisi venenatis", "1"),
("42", "2015-12-25 00:00:00", "43", "4", "lobortis sapien sapien non mi integer ac neque duis bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui vel nisl duis ac nibh fusce lacus purus", "1"),
("43", "2015-12-25 00:00:00", "96", "1", "sagittis dui vel nisl duis ac nibh fusce lacus purus aliquet at feugiat non pretium quis lectus suspendisse potenti in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh", "1"),
("44", "2015-12-25 00:00:00", "69", "5", "ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede ac diam cras pellentesque volutpat dui maecenas tristique est et tempus semper est quam pharetra magna ac consequat metus sapien ut", "0"),
("45", "2015-12-25 00:00:00", "13", "4", "cursus urna ut tellus nulla ut erat id mauris vulputate elementum nullam varius nulla facilisi cras non velit nec nisi vulputate nonummy maecenas tincidunt lacus at velit vivamus vel nulla eget eros elementum pellentesque quisque porta volutpat erat quisque erat eros viverra eget congue", "0"),
("46", "2015-12-25 00:00:00", "13", "1", "lectus vestibulum quam sapien varius ut blandit non interdum in ante vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae duis faucibus", "1"),
("47", "2015-12-25 00:00:00", "52", "1", "augue luctus tincidunt nulla mollis molestie lorem quisque ut erat curabitur gravida nisi at nibh in hac habitasse platea dictumst aliquam augue quam sollicitudin vitae consectetuer eget rutrum at lorem integer tincidunt ante vel ipsum praesent", "1"),
("48", "2015-12-25 00:00:00", "88", "1", "velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae donec pharetra magna vestibulum aliquet ultrices erat tortor sollicitudin mi sit amet lobortis sapien sapien non", "1"),
("49", "2015-12-25 00:00:00", "57", "2", "lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor id nulla ultrices aliquet maecenas leo odio condimentum id luctus nec molestie sed justo pellentesque viverra pede", "0"),
("50", "2015-12-25 00:00:00", "78", "1", "nulla suspendisse potenti cras in purus eu magna vulputate luctus cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus vivamus vestibulum sagittis sapien cum sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus mus", "0");

INSERT INTO `poll`(Selected,`Question`, `FirstAnswer`, `SecondAnswer`, `ThirdAnswer`, `FourthAnswer`, `FirstCount`, `SecondCount`, `ThirdCount`, `FourthCount`)
VALUES (1,"What is your native language?", "English", "French", "Spanish", "Other", 15, 10, 30, 10),
        (0,"What is your current status?", "Student", "Employed", "Retired", "Other", 20, 12, 18, 23),
        (0,"How old are you?", "Under 18", "18-30", "31-65", "Over 65", 16, 32, 45, 10),
        (0,"What’s your favorite genre of book?","Travel","Cooking","Science Fiction","Other",29,13,36,45),
        (0,"How often do you read?","Every day","Once a week","Once a month","Other",38,48,47,21),
        (0,"Where do you prefer to read?","At home","On public transit","At a Cafe","Other",13,25,53,2),
        (0,"Who would you recommend a book to?","Your family","Your friends","Your Colleagues","Other",90,34,76,42),
        (0,"How often do you buy books?","Every day","Once a week","Once a month","Other",34,32,57,21),
        (0,"Which device do you prefer to read on?","Tablet","Computer","Smartphone","Other",37,21,45,20),
        (0,"Which e-book format do you prefer?","PDF","EPUB","Mobi","Other",46,24,39,27);


INSERT INTO `feed` (`Name`, `URI`, `SELECTED`)
VALUES
('CBC top story', 'http://rss.cbc.ca/lineup/topstories.xml', '1'),
('CBC healthcare', 'http://rss.cbc.ca/lineup/health.xml', '0');

INSERT INTO `banner` (`URI`, `SELECTED`)
VALUES
('aberfeldy_ad.jpg', '1'),
('johniewalker_ad.JPG', '1'),
('netflix_ad.jpg', '1'),
('osheaga_ad.jpg', '1');


insert into Sales (id, DateEntered, Client, GrossValue, NetValue, Removed, BillingAddress) values 
(1, "2015-12-25 00:00:00", 37, 86.47, 3.77, 1, 1),
(2, "2015-12-25 00:00:00", 100, 93.3, 92.78, 0, 1),
(3, "2015-12-25 00:00:00", 99, 59.24, 91.52, 0, 1),
(4, "2015-12-25 00:00:00", 25, 80.81, 98.18, 1, 1),
(5, "2015-12-25 00:00:00", 72, 24.97, 97.07, 1, 1),
(6, "2015-12-25 00:00:00", 41, 55.44, 11.43, 0, 1),
(7, "2015-12-25 00:00:00", 90, 66.31, 99.29, 1, 1),
(8, "2015-12-25 00:00:00", 30, 32.3, 22.0, 1, 1),
(9, "2015-12-25 00:00:00", 19, 37.18, 76.0, 0, 1),
(10, "2015-12-25 00:00:00", 89, 38.75, 17.32, 1, 1),
(11, "2016-1-05 00:00:00", 98, 27.03, 3.15, 1, 1),
(12, "2016-1-05 00:00:00", 58, 63.47, 66.63, 1, 1),
(13, "2016-1-05 00:00:00", 96, 89.98, 19.18, 1, 1),
(14, "2016-1-05 00:00:00", 37, 27.34, 73.2, 0, 1),
(15, "2016-1-05 00:00:00", 81, 3.65, 72.22, 1, 1),
(16, "2016-1-05 00:00:00", 100, 93.29, 23.8, 1, 1),
(17, "2016-1-05 00:00:00", 98, 70.84, 1.28, 1, 1),
(18, "2016-1-05 00:00:00", 66, 67.23, 76.44, 0, 1),
(19, "2016-1-05 00:00:00", 63, 77.19, 56.85, 0, 1),
(20, "2016-1-05 00:00:00", 73, 58.64, 42.12, 1, 1),
(21, "2016-1-15 00:00:00", 45, 83.14, 34.74, 0, 1),
(22, "2016-1-15 00:00:00", 19, 1.02, 19.02, 0, 1),
(23, "2016-1-15 00:00:00", 50, 20.05, 25.79, 1, 1),
(24, "2016-1-15 00:00:00", 58, 35.08, 62.39, 0, 1),
(25, "2016-1-15 00:00:00", 33, 34.96, 89.76, 0, 1),
(26, "2016-1-15 00:00:00", 94, 83.17, 9.47, 0, 1),
(27, "2016-1-15 00:00:00", 47, 97.6, 23.47, 1, 1),
(28, "2016-1-15 00:00:00", 72, 64.43, 49.48, 1, 1),
(29, "2016-1-15 00:00:00", 30, 58.12, 86.54, 1, 1),
(30, "2016-1-15 00:00:00", 21, 59.58, 86.4, 1, 1),
(31, "2016-1-25 00:00:00", 52, 90.57, 23.4, 0, 1),
(32, "2016-1-25 00:00:00", 29, 36.39, 53.31, 1, 1),
(33, "2016-1-25 00:00:00", 19, 53.86, 23.3, 0, 1),
(34, "2016-1-25 00:00:00", 33, 65.64, 8.84, 0, 1),
(35, "2016-1-25 00:00:00", 33, 56.3, 50.77, 1, 1),
(36, "2016-1-25 00:00:00", 6, 45.76, 24.15, 0, 1),
(37, "2016-1-25 00:00:00", 16, 68.21, 70.11, 0, 1),
(38, "2016-1-25 00:00:00", 52, 61.45, 79.44, 1, 1),
(39, "2016-1-25 00:00:00", 7, 14.8, 75.53, 0, 1),
(40, "2016-1-25 00:00:00", 11, 7.58, 47.82, 0, 1),
(41, "2016-2-05 00:00:00", 99, 34.39, 13.06, 1, 1),
(42, "2016-2-05 00:00:00", 24, 1.64, 49.13, 0, 1),
(43, "2016-2-05 00:00:00", 4, 19.2, 86.4, 1, 1),
(44, "2016-2-05 00:00:00", 1, 20.98, 92.44, 1, 1),
(45, "2016-2-05 00:00:00", 70, 23.17, 19.69, 1, 1),
(46, "2016-2-05 00:00:00", 19, 97.33, 45.53, 0, 1),
(47, "2016-2-05 00:00:00", 66, 95.46, 7.82, 1, 1),
(48, "2016-2-05 00:00:00", 67, 58.03, 8.92, 0, 1),
(49, "2016-2-05 00:00:00", 74, 53.7, 16.38, 1, 1),
(50, "2016-2-05 00:00:00", 95, 36.89, 74.53, 1, 1),
(51, "2016-2-15 00:00:00", 50, 90.81, 71.01, 1, 1),
(52, "2016-2-15 00:00:00", 34, 50.64, 20.16, 1, 1),
(53, "2016-2-15 00:00:00", 29, 22.2, 25.98, 1, 1),
(54, "2016-2-15 00:00:00", 85, 68.98, 19.57, 1, 1),
(55, "2016-2-15 00:00:00", 64, 98.34, 80.69, 0, 1),
(56, "2016-2-15 00:00:00", 92, 71.49, 19.97, 1, 1),
(57, "2016-2-15 00:00:00", 66, 3.68, 48.91, 1, 1),
(58, "2016-2-15 00:00:00", 11, 14.01, 68.02, 1, 1),
(59, "2016-2-15 00:00:00", 86, 54.25, 51.52, 1, 1),
(60, "2016-2-15 00:00:00", 24, 42.93, 41.31, 1, 1),
(61, "2016-2-25 00:00:00", 8, 20.24, 95.73, 1, 1),
(62, "2016-2-25 00:00:00", 31, 31.27, 52.26, 1, 1),
(63, "2016-2-25 00:00:00", 49, 28.92, 44.41, 1, 1),
(64, "2016-2-25 00:00:00", 93, 64.46, 19.6, 0, 1),
(65, "2016-2-25 00:00:00", 40, 67.57, 86.3, 0, 1),
(66, "2016-2-25 00:00:00", 10, 29.28, 70.95, 0, 1),
(67, "2016-2-25 00:00:00", 81, 88.57, 18.23, 0, 1),
(68, "2016-2-25 00:00:00", 86, 75.55, 6.08, 0, 1),
(69, "2016-2-25 00:00:00", 61, 37.14, 31.81, 1, 1),
(70, "2016-2-25 00:00:00", 41, 82.77, 23.33, 1, 1),
(71, "2016-3-05 00:00:00", 57, 8.58, 96.29, 0, 1),
(72, "2016-3-05 00:00:00", 85, 94.45, 98.43, 1, 1),
(73, "2016-3-05 00:00:00", 7, 79.68, 71.91, 1, 1),
(74, "2016-3-05 00:00:00", 20, 22.13, 86.83, 0, 1),
(75, "2016-3-05 00:00:00", 39, 50.35, 49.22, 1, 1),
(76, "2016-3-05 00:00:00", 76, 25.43, 51.92, 1, 1),
(77, "2016-3-05 00:00:00", 57, 2.13, 13.31, 1, 1),
(78, "2016-3-05 00:00:00", 56, 87.51, 32.04, 1, 1),
(79, "2016-3-05 00:00:00", 60, 93.85, 54.55, 0, 1),
(80, "2016-3-05 00:00:00", 34, 68.48, 87.96, 0, 1),
(81, "2016-3-10 00:00:00", 49, 53.88, 89.0, 1, 1),
(82, "2016-3-10 00:00:00", 48, 72.6, 18.69, 0, 1),
(83, "2016-3-10 00:00:00", 99, 63.52, 55.68, 1, 1),
(84, "2016-3-10 00:00:00", 16, 74.48, 28.72, 0, 1),
(85, "2016-3-10 00:00:00", 86, 67.62, 42.87, 1, 1),
(86, "2016-3-10 00:00:00", 56, 57.27, 33.99, 0, 1),
(87, "2016-3-10 00:00:00", 21, 70.65, 37.17, 0, 1),
(88, "2016-3-10 00:00:00", 92, 55.98, 87.69, 0, 1),
(89, "2016-3-10 00:00:00", 70, 82.98, 15.16, 1, 1),
(90, "2016-3-10 00:00:00", 44, 71.97, 27.62, 1, 1),
(91, "2016-3-15 00:00:00", 90, 41.67, 57.46, 1, 1),
(92, "2016-3-15 00:00:00", 34, 60.91, 86.78, 0, 1),
(93, "2016-3-15 00:00:00", 56, 59.61, 18.53, 0, 1),
(94, "2016-3-15 00:00:00", 32, 44.38, 63.77, 1, 1),
(95, "2016-3-15 00:00:00", 79, 86.69, 67.46, 0, 1),
(96, "2016-3-15 00:00:00", 34, 71.37, 33.62, 1, 1),
(97, "2016-3-15 00:00:00", 95, 87.5, 18.83, 0, 1),
(98, "2016-3-15 00:00:00", 95, 38.31, 81.04, 0, 1),
(99, "2016-3-15 00:00:00", 12, 43.27, 40.47, 0, 1),
(100, "2016-3-15 00:00:00", 8, 27.91, 71.94, 0, 1),
(101, "2016-3-20 00:00:00", 71, 89.31, 50.08, 0, 1),
(102, "2016-3-20 00:00:00", 83, 35.08, 19.78, 1, 1),
(103, "2016-3-20 00:00:00", 93, 24.93, 8.5, 0, 1),
(104, "2016-3-20 00:00:00", 64, 77.42, 74.1, 0, 1),
(105, "2016-3-20 00:00:00", 3, 26.48, 49.73, 0, 1),
(106, "2016-3-20 00:00:00", 63, 14.63, 78.6, 0, 1),
(107, "2016-3-20 00:00:00", 23, 16.27, 93.32, 1, 1),
(108, "2016-3-20 00:00:00", 97, 66.05, 91.56, 0, 1),
(109, "2016-3-20 00:00:00", 89, 40.29, 79.05, 0, 1),
(110, "2016-3-20 00:00:00", 18, 27.13, 88.1, 1, 1),
(111, "2016-3-20 00:00:00", 82, 80.46, 39.12, 1, 1),
(112, "2016-3-20 00:00:00", 43, 75.58, 54.76, 0, 1),
(113, "2016-3-20 00:00:00", 66, 81.41, 55.36, 0, 1),
(114, "2016-3-20 00:00:00", 34, 96.61, 23.62, 1, 1),
(115, "2016-3-20 00:00:00", 30, 52.97, 83.28, 0, 1),
(116, "2016-3-25 00:00:00", 49, 51.04, 35.74, 1, 1),
(117, "2016-3-25 00:00:00", 23, 54.27, 82.72, 1, 1),
(118, "2016-3-25 00:00:00", 80, 32.43, 22.88, 0, 1),
(119, "2016-3-25 00:00:00", 74, 31.28, 65.21, 1, 1),
(120, "2016-3-25 00:00:00", 25, 31.86, 97.27, 1, 1),
(121, "2016-3-25 00:00:00", 56, 64.73, 29.68, 0, 1),
(122, "2016-3-25 00:00:00", 99, 63.17, 57.35, 1, 1),
(123, "2016-3-25 00:00:00", 71, 71.25, 27.6, 1, 1),
(124, "2016-3-25 00:00:00", 10, 67.66, 33.17, 1, 1),
(125, "2016-3-25 00:00:00", 89, 24.39, 3.89, 1, 1),
(126, "2016-3-25 00:00:00", 24, 86.26, 3.17, 1, 1),
(127, "2016-3-25 00:00:00", 69, 75.09, 17.9, 1, 1),
(128, "2016-3-25 00:00:00", 85, 9.99, 24.95, 1, 1),
(129, "2016-3-25 00:00:00", 22, 39.15, 61.07, 0, 1),
(130, "2016-3-25 00:00:00", 94, 75.02, 57.86, 1, 1),
(131, "2016-3-30 00:00:00", 86, 35.88, 12.13, 0, 1),
(132, "2016-3-30 00:00:00", 81, 46.6, 74.64, 1, 1),
(133, "2016-3-30 00:00:00", 94, 27.47, 21.02, 0, 1),
(134, "2016-3-30 00:00:00", 4, 35.65, 73.44, 1, 1),
(135, "2016-3-30 00:00:00", 31, 42.41, 12.94, 1, 1),
(136, "2016-3-30 00:00:00", 26, 33.07, 9.11, 0, 1),
(137, "2016-3-30 00:00:00", 48, 89.51, 45.12, 1, 1),
(138, "2016-3-30 00:00:00", 89, 56.99, 84.1, 1, 1),
(139, "2016-3-30 00:00:00", 53, 10.6, 23.22, 0, 1),
(140, CURRENT_TIMESTAMP, 64, 65.81, 8.77, 1, 1),
(141, CURRENT_TIMESTAMP, 29, 69.08, 93.91, 0, 1),
(142, CURRENT_TIMESTAMP, 45, 36.37, 16.74, 1, 1),
(143, CURRENT_TIMESTAMP, 36, 90.52, 68.93, 1, 1),
(144, CURRENT_TIMESTAMP, 90, 39.46, 67.04, 0, 1),
(145, CURRENT_TIMESTAMP, 11, 38.12, 39.75, 1, 1),
(146, CURRENT_TIMESTAMP, 37, 59.67, 43.21, 0, 1),
(147, CURRENT_TIMESTAMP, 30, 36.54, 40.51, 0, 1),
(148, CURRENT_TIMESTAMP, 27, 53.21, 17.26, 0, 1),
(149, CURRENT_TIMESTAMP, 46, 4.39, 50.28, 1, 1),
(150, CURRENT_TIMESTAMP, 74, 84.21, 38.46, 1, 1);


insert into SalesDetails (id, Sale, Book, Price, PST, HST, GST, Removed) values
(1, 9, 72, 30.28, 0.11, 0.0, 0.03, 1)
, (2, 60, 32, 57.52, 0.12, 0.09, 0.07, 0)
, (3, 135, 30, 44.96, 0.13, 0.13, 0.15, 0)
, (4, 99, 25, 14.39, 0.13, 0.06, 0.11, 1)
, (5, 22, 50, 14.35, 0.13, 0.08, 0.1, 0)
, (6, 111, 29, 34.3, 0.13, 0.12, 0.06, 1)
, (7, 45, 53, 41.26, 0.07, 0.03, 0.03, 1)
, (8, 122, 7, 39.87, 0.12, 0.09, 0.01, 1)
, (9, 40, 58, 25.09, 0.03, 0.12, 0.05, 1)
, (10, 38, 61, 43.45, 0.02, 0.07, 0.07, 0)
, (11, 34, 22, 9.89, 0.13, 0.01, 0.11, 0)
, (12, 35, 6, 28.49, 0.12, 0.07, 0.04, 1)
, (13, 6, 38, 56.81, 0.13, 0.07, 0.04, 1)
, (14, 131, 21, 36.45, 0.05, 0.01, 0.03, 0)
, (15, 67, 3, 41.6, 0.03, 0.13, 0.03, 1)
, (16, 88, 48, 55.41, 0.09, 0.03, 0.05, 0)
, (17, 59, 96, 52.69, 0.14, 0.01, 0.01, 0)
, (18, 78, 79, 49.0, 0.06, 0.06, 0.14, 1)
, (19, 115, 16, 30.28, 0.12, 0.01, 0.09, 1)
, (20, 128, 19, 5.67, 0.03, 0.11, 0.14, 0)
, (21, 25, 45, 3.48, 0.02, 0.05, 0.1, 0)
, (22, 34, 15, 57.48, 0.08, 0.11, 0.11, 1)
, (23, 96, 9, 30.68, 0.05, 0.11, 0.13, 1)
, (24, 64, 82, 58.61, 0.03, 0.03, 0.02, 0)
, (25, 98, 77, 38.44, 0.04, 0.08, 0.04, 1)
, (26, 2, 26, 3.65, 0.06, 0.05, 0.12, 0)
, (27, 13, 50, 57.58, 0.0, 0.07, 0.13, 1)
, (28, 48, 23, 17.86, 0.05, 0.01, 0.05, 1)
, (29, 110, 4, 27.46, 0.04, 0.14, 0.14, 0)
, (30, 49, 60, 48.06, 0.1, 0.13, 0.11, 0)
, (31, 43, 89, 23.37, 0.03, 0.12, 0.09, 1)
, (32, 102, 88, 17.83, 0.13, 0.08, 0.13, 1)
, (33, 17, 22, 44.55, 0.11, 0.13, 0.13, 1)
, (34, 12, 13, 57.55, 0.0, 0.09, 0.07, 0)
, (35, 143, 6, 47.48, 0.07, 0.08, 0.14, 0)
, (36, 59, 80, 26.92, 0.04, 0.11, 0.01, 0)
, (37, 65, 30, 19.68, 0.11, 0.11, 0.04, 0)
, (38, 125, 96, 29.15, 0.07, 0.01, 0.12, 1)
, (39, 22, 50, 39.6, 0.14, 0.03, 0.09, 1)
, (40, 48, 41, 11.91, 0.0, 0.09, 0.09, 1)
, (41, 16, 52, 30.65, 0.12, 0.05, 0.09, 1)
, (42, 101, 60, 48.55, 0.02, 0.07, 0.09, 1)
, (43, 8, 74, 31.86, 0.12, 0.11, 0.14, 0)
, (44, 107, 2, 8.59, 0.02, 0.15, 0.14, 1)
, (45, 43, 43, 10.16, 0.01, 0.09, 0.13, 1)
, (46, 19, 48, 28.98, 0.08, 0.02, 0.05, 0)
, (47, 146, 10, 47.92, 0.05, 0.02, 0.07, 0)
, (48, 58, 21, 24.01, 0.09, 0.01, 0.14, 1)
, (49, 18, 15, 44.6, 0.1, 0.07, 0.11, 1)
, (50, 31, 81, 42.25, 0.0, 0.01, 0.14, 0)
, (51, 147, 62, 41.11, 0.08, 0.01, 0.03, 0)
, (52, 109, 80, 13.32, 0.14, 0.13, 0.07, 0)
, (53, 147, 6, 26.33, 0.14, 0.14, 0.11, 1)
, (54, 8, 74, 54.67, 0.01, 0.13, 0.09, 1)
, (55, 79, 10, 41.17, 0.11, 0.03, 0.02, 0)
, (56, 5, 90, 37.65, 0.13, 0.08, 0.08, 0)
, (57, 57, 18, 34.55, 0.13, 0.03, 0.07, 0)
, (58, 112, 77, 41.09, 0.11, 0.13, 0.04, 0)
, (59, 72, 51, 40.04, 0.01, 0.04, 0.13, 1)
, (60, 52, 44, 58.61, 0.12, 0.05, 0.04, 1)
, (61, 101, 64, 50.51, 0.01, 0.0, 0.15, 0)
, (62, 66, 38, 12.63, 0.14, 0.04, 0.04, 0)
, (63, 74, 37, 20.04, 0.08, 0.03, 0.03, 0)
, (64, 95, 61, 34.73, 0.06, 0.04, 0.05, 0)
, (65, 148, 61, 28.01, 0.04, 0.12, 0.14, 0)
, (66, 31, 85, 6.59, 0.1, 0.07, 0.09, 1)
, (67, 150, 63, 24.87, 0.07, 0.12, 0.05, 0)
, (68, 24, 27, 12.86, 0.12, 0.12, 0.09, 1)
, (69, 45, 37, 53.66, 0.1, 0.08, 0.14, 1)
, (70, 38, 60, 28.85, 0.03, 0.12, 0.05, 1)
, (71, 124, 80, 19.29, 0.08, 0.1, 0.11, 1)
, (72, 94, 4, 54.36, 0.05, 0.04, 0.09, 0)
, (73, 144, 65, 56.07, 0.02, 0.14, 0.13, 1)
, (74, 44, 13, 44.11, 0.13, 0.03, 0.03, 0)
, (75, 115, 86, 23.21, 0.05, 0.06, 0.09, 0)
, (76, 39, 5, 24.14, 0.03, 0.11, 0.15, 1)
, (77, 62, 11, 48.09, 0.12, 0.14, 0.13, 0)
, (78, 140, 93, 4.88, 0.14, 0.09, 0.02, 0)
, (79, 62, 9, 16.56, 0.02, 0.07, 0.07, 0)
, (80, 15, 31, 21.15, 0.1, 0.13, 0.07, 1)
, (81, 140, 61, 46.79, 0.09, 0.11, 0.07, 0)
, (82, 20, 84, 11.47, 0.1, 0.06, 0.04, 0)
, (83, 103, 19, 53.63, 0.04, 0.07, 0.01, 1)
, (84, 118, 79, 41.83, 0.12, 0.09, 0.02, 1)
, (85, 48, 33, 20.31, 0.11, 0.02, 0.06, 1)
, (86, 74, 30, 34.29, 0.12, 0.08, 0.13, 0)
, (87, 42, 70, 5.16, 0.0, 0.03, 0.08, 0)
, (88, 130, 76, 32.41, 0.02, 0.12, 0.11, 0)
, (89, 19, 99, 43.27, 0.01, 0.06, 0.03, 0)
, (90, 106, 90, 51.04, 0.02, 0.1, 0.07, 1)
, (91, 15, 39, 34.72, 0.05, 0.03, 0.06, 1)
, (92, 56, 1, 8.94, 0.05, 0.12, 0.05, 1)
, (93, 35, 55, 13.0, 0.04, 0.09, 0.11, 1)
, (94, 150, 79, 48.36, 0.12, 0.13, 0.01, 1)
, (95, 145, 21, 30.36, 0.12, 0.07, 0.11, 1)
, (96, 39, 84, 56.44, 0.11, 0.02, 0.13, 1)
, (97, 128, 26, 30.33, 0.02, 0.05, 0.06, 0)
, (98, 129, 40, 36.72, 0.13, 0.02, 0.12, 1)
, (99, 103, 5, 20.7, 0.0, 0.04, 0.0, 1)
, (100, 100, 62, 54.2, 0.03, 0.13, 0.1, 0)
, (101, 77, 48, 6.72, 0.11, 0.01, 0.13, 0)
, (102, 95, 33, 27.25, 0.12, 0.04, 0.11, 1)
, (103, 27, 50, 23.92, 0.0, 0.05, 0.02, 1)
, (104, 65, 79, 20.0, 0.15, 0.02, 0.14, 1)
, (105, 109, 11, 8.51, 0.03, 0.01, 0.08, 0)
, (106, 24, 61, 4.88, 0.01, 0.12, 0.02, 1)
, (107, 3, 77, 27.47, 0.03, 0.09, 0.09, 1)
, (108, 13, 36, 38.15, 0.11, 0.01, 0.14, 1)
, (109, 101, 11, 56.78, 0.12, 0.12, 0.13, 0)
, (110, 90, 42, 42.2, 0.14, 0.1, 0.07, 1)
, (111, 113, 40, 48.44, 0.02, 0.11, 0.07, 0)
, (112, 57, 31, 14.42, 0.01, 0.13, 0.07, 1)
, (113, 109, 100, 54.57, 0.15, 0.14, 0.09, 1)
, (114, 62, 14, 4.2, 0.07, 0.13, 0.12, 1)
, (115, 47, 25, 29.65, 0.1, 0.09, 0.11, 0)
, (116, 128, 8, 7.31, 0.04, 0.09, 0.12, 0)
, (117, 25, 92, 8.55, 0.13, 0.1, 0.02, 1)
, (118, 3, 6, 34.3, 0.13, 0.07, 0.13, 1)
, (119, 137, 30, 57.71, 0.14, 0.1, 0.07, 0)
, (120, 48, 92, 13.06, 0.14, 0.14, 0.12, 1)
, (121, 139, 30, 14.08, 0.03, 0.05, 0.03, 1)
, (122, 125, 82, 36.54, 0.02, 0.1, 0.14, 0)
, (123, 20, 57, 46.86, 0.11, 0.06, 0.05, 0)
, (124, 133, 60, 6.15, 0.12, 0.04, 0.08, 1)
, (125, 36, 74, 41.77, 0.12, 0.14, 0.07, 1)
, (126, 131, 39, 13.59, 0.14, 0.03, 0.02, 0)
, (127, 125, 1, 27.04, 0.14, 0.14, 0.13, 0)
, (128, 32, 3, 29.37, 0.02, 0.05, 0.03, 1)
, (129, 137, 19, 49.27, 0.11, 0.07, 0.11, 1)
, (130, 108, 54, 12.16, 0.06, 0.1, 0.01, 1)
, (131, 60, 89, 28.18, 0.03, 0.02, 0.05, 0)
, (132, 48, 49, 42.49, 0.11, 0.13, 0.11, 0)
, (133, 19, 28, 40.56, 0.0, 0.14, 0.07, 0)
, (134, 91, 59, 24.42, 0.05, 0.13, 0.09, 1)
, (135, 3, 19, 53.96, 0.03, 0.0, 0.01, 0)
, (136, 32, 76, 16.37, 0.07, 0.1, 0.08, 1)
, (137, 87, 62, 24.05, 0.15, 0.12, 0.03, 1)
, (138, 117, 67, 12.26, 0.09, 0.07, 0.03, 1)
, (139, 45, 45, 55.18, 0.15, 0.09, 0.09, 1)
, (140, 143, 7, 56.34, 0.02, 0.1, 0.01, 0)
, (141, 124, 88, 52.93, 0.13, 0.1, 0.1, 1)
, (142, 36, 100, 28.56, 0.09, 0.11, 0.09, 1)
, (143, 68, 1, 50.41, 0.03, 0.03, 0.12, 0)
, (144, 138, 50, 58.75, 0.11, 0.06, 0.13, 0)
, (145, 97, 35, 20.88, 0.02, 0.12, 0.15, 0)
, (146, 64, 15, 35.85, 0.04, 0.06, 0.13, 1)
, (147, 118, 52, 24.86, 0.08, 0.15, 0.15, 1)
, (148, 22, 100, 17.04, 0.15, 0.02, 0.07, 1)
, (149, 123, 40, 40.74, 0.14, 0.0, 0.12, 0)
, (150, 75, 100, 20.99, 0.12, 0.07, 0.07, 0)
, (151, 110, 6, 43.07, 0.05, 0.11, 0.01, 1)
, (152, 89, 33, 29.52, 0.14, 0.13, 0.07, 1)
, (153, 50, 56, 53.54, 0.08, 0.04, 0.09, 1)
, (154, 139, 22, 55.9, 0.09, 0.07, 0.12, 1)
, (155, 109, 49, 31.59, 0.02, 0.09, 0.0, 1)
, (156, 45, 5, 56.36, 0.09, 0.02, 0.01, 0)
, (157, 94, 9, 5.62, 0.08, 0.14, 0.07, 1)
, (158, 52, 12, 9.95, 0.13, 0.08, 0.04, 0)
, (159, 28, 31, 37.54, 0.03, 0.13, 0.02, 1)
, (160, 33, 66, 1.71, 0.05, 0.13, 0.14, 0)
, (161, 100, 56, 13.83, 0.04, 0.0, 0.07, 0)
, (162, 89, 49, 55.15, 0.09, 0.01, 0.12, 0)
, (163, 104, 10, 49.27, 0.07, 0.05, 0.06, 0)
, (164, 138, 18, 50.1, 0.13, 0.12, 0.03, 1)
, (165, 137, 92, 26.35, 0.06, 0.04, 0.1, 0)
, (166, 93, 48, 21.36, 0.07, 0.15, 0.02, 1)
, (167, 35, 85, 15.89, 0.1, 0.09, 0.08, 1)
, (168, 33, 25, 25.51, 0.05, 0.04, 0.01, 0)
, (169, 28, 14, 26.96, 0.1, 0.11, 0.08, 0)
, (170, 79, 53, 45.9, 0.1, 0.06, 0.11, 1)
, (171, 15, 85, 31.13, 0.01, 0.1, 0.02, 0)
, (172, 65, 81, 1.46, 0.08, 0.14, 0.03, 1)
, (173, 23, 91, 48.78, 0.04, 0.15, 0.1, 1)
, (174, 106, 45, 24.17, 0.06, 0.14, 0.12, 0)
, (175, 95, 1, 16.01, 0.09, 0.04, 0.07, 1)
, (176, 47, 31, 37.08, 0.03, 0.08, 0.13, 1)
, (177, 120, 84, 49.34, 0.15, 0.01, 0.12, 1)
, (178, 50, 38, 16.89, 0.09, 0.13, 0.01, 0)
, (179, 121, 78, 31.63, 0.0, 0.15, 0.0, 1)
, (180, 105, 68, 9.25, 0.12, 0.15, 0.0, 1)
, (181, 82, 90, 5.92, 0.02, 0.05, 0.13, 0)
, (182, 60, 73, 53.96, 0.0, 0.07, 0.06, 1)
, (183, 138, 18, 2.55, 0.04, 0.03, 0.12, 0)
, (184, 56, 43, 8.65, 0.03, 0.09, 0.0, 1)
, (185, 54, 54, 53.97, 0.15, 0.04, 0.13, 0)
, (186, 108, 68, 4.86, 0.14, 0.02, 0.05, 0)
, (187, 83, 61, 38.41, 0.05, 0.07, 0.1, 0)
, (188, 53, 92, 43.42, 0.14, 0.11, 0.02, 0)
, (189, 86, 61, 7.55, 0.09, 0.03, 0.05, 0)
, (190, 17, 96, 29.01, 0.07, 0.08, 0.13, 1)
, (191, 24, 5, 10.67, 0.11, 0.03, 0.05, 1)
, (192, 87, 30, 14.11, 0.1, 0.13, 0.01, 0)
, (193, 9, 73, 25.82, 0.05, 0.06, 0.15, 1)
, (194, 149, 54, 51.27, 0.02, 0.06, 0.05, 1)
, (195, 34, 79, 20.07, 0.11, 0.09, 0.03, 0)
, (196, 110, 27, 34.62, 0.02, 0.08, 0.03, 1)
, (197, 105, 44, 49.17, 0.06, 0.07, 0.06, 1)
, (198, 135, 21, 41.19, 0.12, 0.1, 0.05, 1)
, (199, 138, 53, 6.66, 0.1, 0.06, 0.02, 1)
, (200, 32, 51, 39.64, 0.0, 0.13, 0.03, 1)
, (201, 72, 23, 30.8, 0.1, 0.13, 0.09, 1)
, (202, 125, 70, 50.33, 0.1, 0.13, 0.12, 0)
, (203, 145, 48, 41.48, 0.07, 0.04, 0.09, 0)
, (204, 147, 5, 5.3, 0.11, 0.01, 0.05, 0)
, (205, 39, 66, 53.37, 0.05, 0.06, 0.09, 0)
, (206, 20, 40, 28.13, 0.08, 0.02, 0.09, 1)
, (207, 89, 49, 14.46, 0.14, 0.12, 0.11, 1)
, (208, 46, 78, 8.71, 0.01, 0.01, 0.01, 1)
, (209, 105, 1, 33.67, 0.15, 0.08, 0.12, 0)
, (210, 49, 25, 53.03, 0.15, 0.01, 0.13, 0)
, (211, 12, 30, 42.46, 0.06, 0.01, 0.0, 0)
, (212, 148, 76, 14.31, 0.12, 0.03, 0.01, 1)
, (213, 141, 13, 26.23, 0.05, 0.05, 0.02, 0)
, (214, 19, 17, 23.45, 0.07, 0.02, 0.12, 1)
, (215, 59, 53, 11.53, 0.14, 0.12, 0.1, 0)
, (216, 118, 7, 32.93, 0.13, 0.06, 0.15, 1)
, (217, 35, 98, 18.81, 0.04, 0.03, 0.03, 0)
, (218, 41, 48, 13.8, 0.06, 0.12, 0.12, 0)
, (219, 29, 69, 59.41, 0.12, 0.12, 0.01, 1)
, (220, 106, 23, 49.86, 0.09, 0.01, 0.03, 1)
, (221, 49, 31, 15.44, 0.09, 0.05, 0.1, 1)
, (222, 88, 74, 28.05, 0.15, 0.02, 0.02, 1)
, (223, 146, 95, 9.74, 0.06, 0.09, 0.13, 0)
, (224, 43, 80, 9.46, 0.02, 0.13, 0.04, 0)
, (225, 146, 71, 43.09, 0.05, 0.15, 0.13, 0)
, (226, 85, 18, 46.15, 0.03, 0.15, 0.13, 0)
, (227, 92, 19, 24.18, 0.08, 0.0, 0.04, 1)
, (228, 52, 55, 51.62, 0.05, 0.13, 0.03, 0)
, (229, 57, 57, 25.13, 0.13, 0.11, 0.04, 0)
, (230, 20, 67, 29.8, 0.0, 0.14, 0.13, 0)
, (231, 118, 31, 38.42, 0.01, 0.12, 0.08, 0)
, (232, 147, 38, 22.12, 0.09, 0.13, 0.03, 1)
, (233, 12, 100, 43.13, 0.1, 0.03, 0.08, 1)
, (234, 34, 75, 6.2, 0.1, 0.05, 0.07, 0)
, (235, 8, 8, 17.5, 0.09, 0.12, 0.15, 0)
, (236, 69, 43, 56.83, 0.02, 0.1, 0.14, 1)
, (237, 38, 53, 3.56, 0.1, 0.04, 0.12, 1)
, (238, 102, 45, 6.75, 0.03, 0.08, 0.02, 1)
, (239, 112, 41, 22.75, 0.08, 0.07, 0.03, 1)
, (240, 54, 65, 30.7, 0.13, 0.02, 0.06, 1)
, (241, 74, 2, 45.94, 0.08, 0.09, 0.06, 0)
, (242, 124, 4, 2.51, 0.02, 0.1, 0.08, 0)
, (243, 29, 36, 48.12, 0.0, 0.04, 0.12, 0)
, (244, 123, 85, 23.13, 0.06, 0.06, 0.03, 1)
, (245, 62, 30, 23.83, 0.14, 0.07, 0.13, 1)
, (246, 86, 21, 4.41, 0.1, 0.06, 0.11, 0)
, (247, 41, 56, 38.83, 0.1, 0.07, 0.08, 1)
, (248, 20, 48, 19.93, 0.14, 0.09, 0.04, 0)
, (249, 37, 55, 35.85, 0.02, 0.06, 0.03, 0)
, (250, 90, 65, 10.45, 0.03, 0.11, 0.15, 1)
, (251, 43, 71, 52.99, 0.12, 0.15, 0.08, 1)
, (252, 108, 89, 50.02, 0.05, 0.08, 0.14, 1)
, (253, 40, 97, 53.96, 0.05, 0.13, 0.01, 0)
, (254, 128, 93, 5.19, 0.09, 0.08, 0.08, 0)
, (255, 50, 41, 43.29, 0.1, 0.09, 0.12, 0)
, (256, 5, 31, 34.28, 0.07, 0.0, 0.12, 1)
, (257, 131, 50, 42.66, 0.03, 0.01, 0.04, 0)
, (258, 67, 92, 18.39, 0.06, 0.01, 0.05, 0)
, (259, 52, 14, 25.12, 0.11, 0.13, 0.13, 1)
, (260, 74, 80, 6.43, 0.12, 0.0, 0.11, 1)
, (261, 54, 42, 36.04, 0.0, 0.02, 0.08, 0)
, (262, 82, 32, 5.85, 0.03, 0.08, 0.01, 0)
, (263, 112, 36, 34.02, 0.06, 0.06, 0.13, 1)
, (264, 127, 32, 4.05, 0.07, 0.13, 0.01, 0)
, (265, 73, 71, 49.89, 0.01, 0.07, 0.01, 1)
, (266, 57, 29, 7.76, 0.09, 0.03, 0.1, 0)
, (267, 129, 92, 51.81, 0.12, 0.06, 0.06, 1)
, (268, 73, 5, 42.85, 0.1, 0.1, 0.14, 0)
, (269, 30, 48, 48.35, 0.13, 0.13, 0.05, 0)
, (270, 68, 27, 17.11, 0.08, 0.0, 0.07, 0)
, (271, 61, 91, 7.57, 0.14, 0.02, 0.08, 0)
, (272, 92, 8, 26.45, 0.01, 0.15, 0.09, 1)
, (273, 142, 58, 13.28, 0.05, 0.1, 0.02, 1)
, (274, 76, 62, 25.56, 0.08, 0.06, 0.08, 1)
, (275, 6, 95, 55.34, 0.1, 0.03, 0.03, 0)
, (276, 137, 57, 14.22, 0.11, 0.03, 0.01, 0)
, (277, 11, 60, 14.52, 0.08, 0.03, 0.13, 0)
, (278, 143, 66, 54.27, 0.09, 0.06, 0.14, 1)
, (279, 129, 37, 7.1, 0.07, 0.13, 0.02, 0)
, (280, 49, 92, 49.55, 0.1, 0.13, 0.11, 0)
, (281, 86, 33, 47.88, 0.08, 0.03, 0.08, 1)
, (282, 42, 13, 55.12, 0.04, 0.05, 0.06, 0)
, (283, 103, 33, 35.23, 0.05, 0.12, 0.12, 0)
, (284, 7, 22, 19.26, 0.06, 0.05, 0.12, 0)
, (285, 11, 91, 27.53, 0.12, 0.05, 0.13, 1)
, (286, 99, 80, 41.44, 0.11, 0.1, 0.0, 1)
, (287, 85, 51, 52.32, 0.15, 0.15, 0.06, 1)
, (288, 136, 43, 10.0, 0.0, 0.12, 0.09, 1)
, (289, 60, 90, 20.42, 0.04, 0.15, 0.05, 0)
, (290, 60, 91, 40.99, 0.14, 0.13, 0.09, 0)
, (291, 51, 85, 52.63, 0.08, 0.11, 0.14, 1)
, (292, 109, 85, 23.12, 0.1, 0.02, 0.13, 0)
, (293, 148, 38, 27.8, 0.03, 0.03, 0.09, 1)
, (294, 135, 100, 19.12, 0.13, 0.1, 0.01, 0)
, (295, 78, 10, 31.53, 0.08, 0.01, 0.14, 0)
, (296, 16, 27, 50.98, 0.04, 0.15, 0.11, 0)
, (297, 82, 23, 45.92, 0.07, 0.01, 0.0, 1)
, (298, 60, 85, 22.56, 0.04, 0.02, 0.13, 0)
, (299, 109, 86, 32.29, 0.04, 0.08, 0.01, 1)
, (300, 42, 29, 4.95, 0.06, 0.03, 0.12, 0)
, (301, 5, 41, 37.56, 0.13, 0.07, 0.04, 0)
, (302, 90, 59, 19.84, 0.02, 0.13, 0.06, 1)
, (303, 148, 19, 14.72, 0.11, 0.14, 0.01, 0)
, (304, 67, 94, 9.27, 0.09, 0.04, 0.04, 1)
, (305, 62, 38, 57.13, 0.03, 0.07, 0.14, 0)
, (306, 104, 78, 1.18, 0.06, 0.13, 0.13, 0)
, (307, 29, 86, 58.67, 0.13, 0.1, 0.09, 1)
, (308, 36, 60, 22.45, 0.09, 0.08, 0.07, 0)
, (309, 122, 61, 29.73, 0.05, 0.13, 0.11, 1)
, (310, 67, 75, 15.96, 0.07, 0.09, 0.12, 0)
, (311, 148, 59, 52.31, 0.04, 0.01, 0.04, 1)
, (312, 99, 30, 12.68, 0.13, 0.11, 0.09, 1)
, (313, 131, 61, 11.54, 0.03, 0.08, 0.01, 1)
, (314, 113, 46, 10.04, 0.03, 0.0, 0.08, 0)
, (315, 70, 41, 36.62, 0.05, 0.04, 0.03, 0)
, (316, 28, 53, 9.16, 0.02, 0.04, 0.11, 1)
, (317, 75, 69, 50.98, 0.1, 0.02, 0.05, 0)
, (318, 97, 58, 52.97, 0.1, 0.15, 0.04, 0)
, (319, 49, 98, 49.31, 0.01, 0.08, 0.09, 0)
, (320, 81, 46, 55.34, 0.11, 0.04, 0.03, 0)
, (321, 105, 25, 17.18, 0.08, 0.11, 0.03, 1)
, (322, 86, 93, 19.47, 0.13, 0.15, 0.06, 0)
, (323, 10, 26, 37.61, 0.09, 0.02, 0.09, 0)
, (324, 97, 93, 17.29, 0.06, 0.14, 0.14, 0)
, (325, 15, 9, 40.36, 0.0, 0.12, 0.13, 0)
, (326, 144, 52, 48.58, 0.11, 0.11, 0.11, 0)
, (327, 82, 84, 44.84, 0.05, 0.09, 0.07, 0)
, (328, 113, 15, 33.26, 0.06, 0.14, 0.03, 0)
, (329, 108, 62, 18.71, 0.06, 0.05, 0.03, 1)
, (330, 150, 52, 10.79, 0.14, 0.08, 0.01, 1)
, (331, 106, 65, 54.1, 0.11, 0.05, 0.02, 0)
, (332, 77, 43, 20.2, 0.01, 0.02, 0.02, 0)
, (333, 129, 7, 1.36, 0.1, 0.02, 0.08, 1)
, (334, 67, 95, 11.92, 0.11, 0.1, 0.1, 1)
, (335, 131, 29, 41.44, 0.13, 0.02, 0.09, 1)
, (336, 4, 61, 3.72, 0.07, 0.12, 0.01, 0)
, (337, 2, 93, 28.52, 0.07, 0.04, 0.09, 0)
, (338, 140, 44, 48.21, 0.04, 0.1, 0.14, 1)
, (339, 120, 14, 17.73, 0.03, 0.12, 0.09, 1)
, (340, 18, 31, 50.12, 0.11, 0.11, 0.1, 0)
, (341, 4, 7, 37.01, 0.03, 0.06, 0.11, 1)
, (342, 5, 42, 15.39, 0.07, 0.14, 0.06, 1)
, (343, 59, 69, 58.82, 0.06, 0.1, 0.1, 1)
, (344, 27, 13, 10.25, 0.07, 0.14, 0.12, 1)
, (345, 150, 4, 49.84, 0.08, 0.13, 0.12, 0)
, (346, 1, 2, 45.26, 0.01, 0.13, 0.02, 1)
, (347, 86, 91, 47.52, 0.1, 0.03, 0.0, 0)
, (348, 76, 34, 29.67, 0.09, 0.14, 0.04, 1)
, (349, 50, 11, 6.85, 0.03, 0.14, 0.04, 0)
, (350, 117, 92, 11.8, 0.13, 0.11, 0.14, 0)
, (351, 28, 23, 47.77, 0.04, 0.12, 0.13, 0)
, (352, 68, 36, 46.82, 0.11, 0.05, 0.09, 1)
, (353, 140, 4, 59.46, 0.05, 0.13, 0.12, 1)
, (354, 137, 4, 10.73, 0.05, 0.15, 0.03, 0)
, (355, 129, 72, 33.63, 0.14, 0.04, 0.07, 0)
, (356, 104, 68, 41.47, 0.08, 0.06, 0.12, 0)
, (357, 16, 93, 9.11, 0.09, 0.12, 0.05, 0)
, (358, 138, 70, 23.14, 0.05, 0.01, 0.07, 0)
, (359, 41, 81, 13.17, 0.09, 0.07, 0.08, 1)
, (360, 120, 11, 35.86, 0.12, 0.04, 0.06, 0)
, (361, 12, 20, 16.06, 0.1, 0.0, 0.08, 1)
, (362, 93, 100, 30.96, 0.14, 0.13, 0.12, 1)
, (363, 88, 69, 20.62, 0.04, 0.09, 0.1, 1)
, (364, 28, 95, 58.37, 0.09, 0.01, 0.14, 1)
, (365, 133, 86, 3.23, 0.15, 0.07, 0.08, 0)
, (366, 63, 42, 16.4, 0.03, 0.09, 0.02, 1)
, (367, 108, 59, 44.85, 0.05, 0.06, 0.1, 1)
, (368, 101, 8, 9.21, 0.1, 0.12, 0.15, 1)
, (369, 93, 23, 20.15, 0.05, 0.04, 0.02, 1)
, (370, 4, 28, 46.87, 0.02, 0.01, 0.05, 0)
, (371, 20, 44, 17.61, 0.11, 0.1, 0.06, 0)
, (372, 77, 72, 54.08, 0.12, 0.04, 0.03, 0)
, (373, 89, 49, 15.61, 0.03, 0.01, 0.04, 1)
, (374, 85, 45, 3.61, 0.02, 0.14, 0.01, 1)
, (375, 97, 22, 56.31, 0.01, 0.08, 0.04, 1)
, (376, 61, 96, 20.94, 0.08, 0.02, 0.11, 1)
, (377, 103, 21, 29.44, 0.14, 0.09, 0.09, 0)
, (378, 54, 11, 40.04, 0.07, 0.03, 0.08, 1)
, (379, 5, 19, 11.2, 0.04, 0.08, 0.12, 0)
, (380, 78, 9, 28.98, 0.05, 0.02, 0.09, 0)
, (381, 135, 52, 35.46, 0.09, 0.14, 0.15, 1)
, (382, 77, 41, 16.03, 0.1, 0.07, 0.08, 0)
, (383, 102, 48, 46.91, 0.1, 0.05, 0.01, 1)
, (384, 140, 93, 44.92, 0.05, 0.11, 0.15, 0)
, (385, 124, 75, 45.56, 0.04, 0.12, 0.07, 1)
, (386, 106, 44, 55.64, 0.08, 0.11, 0.03, 0)
, (387, 61, 41, 5.29, 0.12, 0.07, 0.02, 0)
, (388, 16, 65, 46.66, 0.07, 0.01, 0.13, 1)
, (389, 50, 98, 14.17, 0.0, 0.07, 0.1, 1)
, (390, 18, 18, 59.21, 0.05, 0.01, 0.14, 1)
, (391, 31, 56, 48.81, 0.07, 0.03, 0.09, 1)
, (392, 86, 44, 21.65, 0.06, 0.13, 0.02, 1)
, (393, 36, 38, 21.93, 0.09, 0.06, 0.02, 1)
, (394, 101, 73, 21.03, 0.11, 0.11, 0.06, 1)
, (395, 63, 9, 21.58, 0.11, 0.02, 0.15, 1)
, (396, 16, 31, 11.66, 0.01, 0.1, 0.03, 1)
, (397, 150, 84, 46.3, 0.01, 0.14, 0.09, 1)
, (398, 44, 99, 10.44, 0.14, 0.15, 0.05, 1)
, (399, 137, 78, 56.33, 0.05, 0.14, 0.01, 1)
, (400, 74, 79, 17.45, 0.14, 0.06, 0.1, 1)
, (401, 16, 26, 57.86, 0.14, 0.11, 0.09, 0)
, (402, 95, 50, 44.02, 0.02, 0.05, 0.05, 1)
, (403, 142, 29, 38.8, 0.0, 0.12, 0.14, 1)
, (404, 32, 40, 6.14, 0.07, 0.05, 0.14, 0)
, (405, 67, 13, 31.28, 0.13, 0.03, 0.04, 0)
, (406, 146, 71, 49.49, 0.03, 0.12, 0.01, 1)
, (407, 126, 1, 38.32, 0.09, 0.02, 0.1, 1)
, (408, 94, 15, 22.76, 0.08, 0.04, 0.09, 1)
, (409, 147, 11, 47.24, 0.1, 0.1, 0.14, 0)
, (410, 136, 39, 31.34, 0.04, 0.1, 0.07, 0)
, (411, 8, 94, 28.93, 0.06, 0.11, 0.08, 0)
, (412, 25, 50, 49.76, 0.14, 0.06, 0.14, 0)
, (413, 49, 75, 20.95, 0.14, 0.11, 0.05, 0)
, (414, 70, 51, 46.45, 0.06, 0.01, 0.02, 0)
, (415, 19, 31, 1.92, 0.07, 0.04, 0.09, 1)
, (416, 68, 52, 6.28, 0.12, 0.05, 0.03, 0)
, (417, 134, 60, 56.75, 0.12, 0.08, 0.14, 1)
, (418, 3, 83, 27.45, 0.04, 0.13, 0.08, 1)
, (419, 68, 61, 5.26, 0.11, 0.01, 0.07, 0)
, (420, 86, 27, 46.58, 0.14, 0.05, 0.09, 1)
, (421, 41, 34, 19.95, 0.0, 0.13, 0.13, 0)
, (422, 72, 54, 20.99, 0.02, 0.07, 0.04, 1)
, (423, 132, 92, 46.26, 0.1, 0.14, 0.09, 1)
, (424, 148, 22, 37.86, 0.11, 0.11, 0.05, 1)
, (425, 132, 100, 39.93, 0.15, 0.05, 0.0, 1)
, (426, 49, 65, 39.41, 0.03, 0.09, 0.06, 1)
, (427, 78, 41, 54.82, 0.08, 0.05, 0.13, 1)
, (428, 145, 70, 20.73, 0.01, 0.06, 0.02, 1)
, (429, 98, 56, 25.5, 0.07, 0.1, 0.14, 1)
, (430, 86, 37, 20.03, 0.0, 0.14, 0.01, 0)
, (431, 16, 86, 32.17, 0.05, 0.06, 0.05, 0)
, (432, 97, 84, 2.15, 0.09, 0.02, 0.13, 1)
, (433, 47, 82, 27.71, 0.02, 0.07, 0.13, 1)
, (434, 146, 90, 1.85, 0.14, 0.1, 0.05, 1)
, (435, 6, 31, 13.77, 0.01, 0.02, 0.12, 0)
, (436, 54, 84, 57.37, 0.13, 0.01, 0.04, 0)
, (437, 62, 61, 51.56, 0.13, 0.04, 0.04, 1)
, (438, 89, 12, 21.69, 0.15, 0.02, 0.07, 1)
, (439, 122, 90, 41.93, 0.02, 0.08, 0.05, 1)
, (440, 98, 84, 23.95, 0.09, 0.15, 0.1, 0)
, (441, 76, 36, 8.36, 0.1, 0.07, 0.11, 1)
, (442, 10, 78, 54.8, 0.01, 0.07, 0.05, 0)
, (443, 44, 99, 28.3, 0.05, 0.1, 0.11, 0)
, (444, 148, 19, 48.53, 0.08, 0.03, 0.09, 0)
, (445, 65, 94, 35.4, 0.14, 0.0, 0.13, 0)
, (446, 83, 23, 42.97, 0.0, 0.12, 0.01, 1)
, (447, 78, 32, 20.89, 0.06, 0.02, 0.01, 0)
, (448, 100, 63, 15.26, 0.02, 0.02, 0.07, 0)
, (449, 55, 39, 51.15, 0.11, 0.04, 0.11, 0)
, (450, 142, 41, 41.32, 0.03, 0.14, 0.09, 1)
, (451, 127, 60, 47.86, 0.12, 0.14, 0.0, 0)
, (452, 78, 36, 39.82, 0.02, 0.13, 0.03, 1)
, (453, 27, 57, 11.78, 0.1, 0.12, 0.09, 0)
, (454, 134, 6, 57.78, 0.12, 0.09, 0.02, 0)
, (455, 83, 84, 9.4, 0.04, 0.01, 0.06, 1)
, (456, 139, 71, 31.79, 0.08, 0.04, 0.12, 0)
, (457, 81, 12, 38.61, 0.03, 0.03, 0.03, 0)
, (458, 3, 79, 21.24, 0.07, 0.06, 0.11, 1)
, (459, 88, 91, 33.94, 0.01, 0.05, 0.1, 1)
, (460, 48, 37, 17.91, 0.13, 0.06, 0.12, 0)
, (461, 85, 29, 31.0, 0.12, 0.02, 0.08, 0)
, (462, 64, 94, 26.06, 0.06, 0.13, 0.08, 1)
, (463, 111, 29, 58.28, 0.02, 0.03, 0.06, 0)
, (464, 135, 81, 58.48, 0.1, 0.06, 0.09, 0)
, (465, 139, 37, 33.14, 0.02, 0.09, 0.0, 0)
, (466, 114, 56, 49.51, 0.11, 0.08, 0.04, 1)
, (467, 100, 70, 58.86, 0.05, 0.09, 0.13, 1)
, (468, 52, 39, 51.36, 0.05, 0.09, 0.07, 0)
, (469, 113, 52, 12.55, 0.13, 0.01, 0.07, 0)
, (470, 102, 47, 42.33, 0.11, 0.13, 0.12, 0)
, (471, 119, 80, 59.76, 0.04, 0.07, 0.13, 1)
, (472, 100, 42, 21.55, 0.14, 0.11, 0.07, 0)
, (473, 8, 39, 29.64, 0.08, 0.02, 0.1, 0)
, (474, 39, 32, 43.71, 0.11, 0.02, 0.03, 0)
, (475, 69, 47, 22.91, 0.05, 0.13, 0.07, 0)
, (476, 81, 55, 36.98, 0.11, 0.15, 0.11, 1)
, (477, 30, 48, 53.12, 0.12, 0.02, 0.08, 0)
, (478, 7, 6, 19.0, 0.08, 0.03, 0.13, 1)
, (479, 144, 7, 58.2, 0.0, 0.03, 0.02, 0)
, (480, 147, 91, 5.98, 0.06, 0.03, 0.09, 0)
, (481, 138, 65, 7.18, 0.1, 0.01, 0.13, 1)
, (482, 85, 26, 18.97, 0.08, 0.03, 0.12, 0)
, (483, 48, 76, 46.8, 0.11, 0.04, 0.06, 1)
, (484, 53, 74, 28.33, 0.14, 0.07, 0.01, 1)
, (485, 31, 44, 54.24, 0.13, 0.12, 0.13, 0)
, (486, 47, 84, 21.06, 0.0, 0.01, 0.11, 1)
, (487, 141, 94, 18.05, 0.05, 0.05, 0.03, 1)
, (488, 20, 23, 4.49, 0.08, 0.01, 0.04, 1)
, (489, 132, 17, 23.07, 0.01, 0.14, 0.14, 0)
, (490, 90, 4, 30.32, 0.01, 0.12, 0.04, 1)
, (491, 127, 49, 57.12, 0.03, 0.14, 0.09, 0)
, (492, 58, 53, 48.54, 0.0, 0.03, 0.11, 0)
, (493, 42, 52, 32.59, 0.14, 0.07, 0.06, 0)
, (494, 135, 12, 49.51, 0.04, 0.12, 0.11, 1)
, (495, 28, 21, 44.01, 0.07, 0.1, 0.02, 0)
, (496, 119, 73, 59.74, 0.08, 0.06, 0.0, 0)
, (497, 77, 99, 10.1, 0.07, 0.07, 0.05, 1)
, (498, 11, 20, 9.06, 0.01, 0.01, 0.03, 1)
, (499, 78, 25, 1.04, 0.06, 0.03, 0.05, 0)
, (500, 115, 59, 3.08, 0.01, 0.14, 0.0, 0);


INSERT INTO `TaxeRates` (`Province`, `HST`, `GST`, `PST`, `Updated`) 
VALUES ('AB', '0.00', '0.05', '0.00', CURRENT_TIMESTAMP),
 ('BC', '0.00', '0.05', '0.07', CURRENT_TIMESTAMP),
 ('MA', '0.00', '0.05', '0.08', CURRENT_TIMESTAMP),
 ('NB', '0.08','0.05',  '0.00', CURRENT_TIMESTAMP),
 ('NL', '0.08','0.05',  '0.00', CURRENT_TIMESTAMP),
 ('NT', '0.00', '0.05', '0.00', CURRENT_TIMESTAMP),
 ('NS', '0.10', '0.05', '0.00', CURRENT_TIMESTAMP),
 ('NU', '0.00', '0.05', '0.00', CURRENT_TIMESTAMP),
 ('ON', '0.08','0.05',  '0.00', CURRENT_TIMESTAMP),
 ('PI', '0.09','0.05',  '0.00', CURRENT_TIMESTAMP),
 ('QC', '0.00', '0.05', '0.10', CURRENT_TIMESTAMP),
 ('SA', '0.00', '0.05', '0.05', CURRENT_TIMESTAMP),
 ('YU', '0.00', '0.05', '0.00', CURRENT_TIMESTAMP);


INSERT INTO `admin` (`username`, `Password`) VALUES ('adminG4', 'adminG4');
INSERT INTO `admin` (`username`, `Password`) VALUES ('DawsonManager', 'collegedawson');
INSERT INTO `admin` (`username`, `Password`) VALUES ('adminG4W16', 'adminG4W16');