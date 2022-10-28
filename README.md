# ClackProject
JavaDoc
C:\Users\ethan\.jdks\openjdk-19.0.1\bin\java.exe "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2022.2.3\lib\idea_rt.jar=60621:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2022.2.3\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\Users\ethan\IdeaProjects\ClackProject\out\production\ClackProject test.TestClackData
messageClackData1 getType(): 2
messageClackData2 getType(): 2
messageClackData3 getType(): 2
fileClackData1 getType(): 3
fileClackData2 getType(): 3

messageClackData1 getUserName(): Anon
messageClackData2 getUserName(): testUser1
messageClackData3 getUserName(): testuser2
fileClackData1 getUserName(): Anon
fileClackData2 getUserName(): testUser2

messageClackData1 getDate(): Fri Oct 28 17:23:50 EDT 2022
messageClackData2 getDate(): Fri Oct 28 17:23:50 EDT 2022
messageClackData3 getDate(): Fri Oct 28 17:23:50 EDT 2022
fileClackData1 getDate(): Fri Oct 28 17:23:50 EDT 2022
fileClackData2 getDate(): Fri Oct 28 17:23:50 EDT 2022

messageClackData1 getData(): 
messageClackData2 getData(): testMessage
messageClackData3 getData(): wsywwtj hnlg shgyduk
fileClackData1 getData(): null
fileClackData2 getData(): null

messageClackData3 getData(String key): testing this message
fileClackData2 getData(String key): 
messageClackData1 hashCode(): -1492967841
messageClackData2 hashCode(): 85351068
messageClackData3 hashCode(): -150348415
fileClackData1 hashCode(): 814117976
fileClackData2 hashCode(): -1290349466

messageClackData1 equals null: false
messageClackData1 equals messageClackData1: true
messageClackData1 equals messageClackData2: false
messageClackData2 equals messageClackData1: false
messageClackData3 equals messageClackData1: false
messageClackData2 equals messageClackData3: false
messageClackData1 equals fileClackData1: false
fileClackData1 equals null: false
fileClackData1 equals fileClackData1: true
fileClackData1 equals fileClackData2: false
fileClackData2 equals fileClackData1: false
fileClackData1 equals messageClackData1: false

messageClackData1:
This instance of MessageClackData has the following properties:
Username: Anon
Type: 2
Date: Fri Oct 28 17:23:50 EDT 2022
Message: 

messageClackData2:
This instance of MessageClackData has the following properties:
Username: testUser1
Type: 2
Date: Fri Oct 28 17:23:50 EDT 2022
Message: testMessage

messageClackData3:
This instance of MessageClackData has the following properties:
Username: testuser2
Type: 2
Date: Fri Oct 28 17:23:50 EDT 2022
Message: wsywwtj hnlg shgyduk

fileClackData1:
This instance of FileClackData has the following properties:
Username: Anon
Type: 3
Date: Fri Oct 28 17:23:50 EDT 2022
File Name: 
File Contents: null

fileClackData2:
This instance of FileClackData has the following properties:
Username: testUser2
Type: 3
Date: Fri Oct 28 17:23:50 EDT 2022
File Name: filename0
File Contents: null


fileClackData1 getFileName(): 
fileClackData2 getFileName(): filename0

Sets the filename of fileClackData1 to be filename1
fileClackData1 getFileName(): filename1
fileClackData1 hashCode(): -923900274
fileClackData1 equals fileClackData1: true
fileClackData1 equals fileClackData2: false
fileClackData1:
This instance of FileClackData has the following properties:
Username: Anon
Type: 3
Date: Fri Oct 28 17:23:50 EDT 2022
File Name: filename1
File Contents: null


Sets the filename of fileClackData2 to be filename2
fileClackData2 getFileName(): filename2
fileClackData2 hashCode(): -1290349404
fileClackData2 equals fileClackData2: true
fileClackData2 equals fileClackData1: false
fileClackData2:
This instance of FileClackData has the following properties:
Username: testUser2
Type: 3
Date: Fri Oct 28 17:23:50 EDT 2022
File Name: filename2
File Contents: null


Process finished with exit code 0
