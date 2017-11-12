# Portal Helper

Client to access grades for MCPS

## Requirements
  - Jsoup
  - Gson
  
## Usage
  - `Portal p = new Portal()`
  - `p.login(username, password)`
  - `p.getGrades()` will parse and store grade data into `p.classes`