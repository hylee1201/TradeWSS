mvn install:install-file -Dfile=edtftpj-pro.jar -DgroupId=com.enterprisedt.net -DartifactId=edtftpjpro -Dversion=3.9.1 -Dpackaging=jar
mvn install:install-file -Dfile=license.jar -DgroupId=com.enterprisedt.net -DartifactId=edtftpjprolicense -Dversion=3.9.1 -Dpackaging=jar

mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar

<Note>
Go to the project lib where license.jar and edtftpjprolicense.jar are located.
e.g. dir cvr/WebContent/WEB-INF/lib and run the command above