# Setup SQL Server 2019 on Docker

## Reason
Can't run SQL Server on my machine (macOS)


## Setup
1. Pull the image by running the command on the Terminal
   - `docker pull mcr.microsoft.com/mssql/server:2019-latest`
2. Run a container from the image 
   1. option 1: execute the following command on CLI:
      - `docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=Checkmysqlsplz&2025' -e "MSSQL_COLLATION=Chinese_Taiwan_Stroke_CI_AS" -p 1433:1433 --name mssqlserver2019 -d mcr.microsoft.com/mssql/server:2019-latest`  
   2. option 2: from docker gui, and fill in environment variables before running:
     - `ACCEPT_EULA` : `Y`
       - required by MS official
     - `SA_PASSWORD` : `Checkmysqlsplz&2025`
     - `MSSQL_COLLATION` : `Chinese_Taiwan_Stroke_CI_AS` (instead of the default `SQL_Latin1_General_CP1_CI_AS`)
       - set to get closer to the Windows environment back in the bootcamp [1]
       - failure to use this collation may cause equality comparisons for NVARCHAR columns to fail unless N-prefixed Unicode literals are used.
3. Now the SQL Server is ready to be connected
4. Connect from wherever you like:
   - [x] VSCode GUI extension (new official recommendation)
   - Azure Data Studio (not recommended: officially deprecated)
   - sqlcmd (command line tool)
   - DbGate GUI
5. Login Info:
   - ID/PSD: `sa/Checkmysqlsplz&2025`
   - Server Name: localhost
   - Port: defaulted to 1433
6. Set up the database, schema and insert the data
7. Follow `03-login-and-user-template.sql` to set up 
   - YourLoginName, YourUserName as `user01`
   - YourStrongPassword as `P@ssw0rd`
   - YourDatabaseName as `STUDIEHUB`
8. Done


## Notes
1. Collation:
  - Docker@macOS: `SQL_Latin1_General_CP1_CI_AS` by default (CI: case-insensitive; AS: accent-sensitive)
    - search on `'あ'` -> can't find `N'あ'` 
  - Windows: Probably something like `Chinese_Taiwan_Stroke_CI_AS`
    - `WHERE literal = 'あ'` ≈ `WHERE literal = N'あ'` (lazy unicode)

  