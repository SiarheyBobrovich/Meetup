- Test settings: src/test/resources/application-test.yml
- Application settings : src/main/resources/



# Endpoints:
## Method: GET
#### /api/v1/meetup/id/{id} 
##### PathVariable:
1) id - Long (1...Long.MAX_VALUE)
- {
    - "id": 1,
    - "topic": "fourth",
    - "description": "fourth",
    - "organization": "fourth",
    - "place": "fourth",
    - "dt_meetup": "2022-10-04T21:46:48.186",
    - "version": 1667846156665
- }
---
## Method: GET
#### /api/v1/meetup/all
##### RequestParam:
1) topic - String, required - false
2) date - String, format: "yyyy-MM-dd", required - false
3) organization - String, required - false
4) sort - enum[TOPIC, ORGANIZATION, PLACE, DT_MEETUP], required - false
- [
  - {
    - "id": 2,
    - "topic": "third",
    - "description": "third",
    - "organization": "third",
    - "place": "third",
    - "dt_meetup": "2022-11-04T10:10:00",
    - "version": 1667840685631
  - }
- ]
---
## Method: POST
#### /api/v1/meetup
##### RequestBody:
- {
  - "topic":"first",
  - "description":"first",
  - "organization":"first",
  - "place":"first",
  - "dt_meetup": "2022-11-04T10:10"
- }
---
## Method: PUT
#### /api/v1/meetup/id/{id}/version/{dt_update}
##### PathVariable:
1) dt_update - Long
##### RequestBody:
- {
  - "topic":"fourth",
  - "description":"fourth",
  - "organization":"fourth",
  - "place":"fourth",
  - "dt_meetup": "2022-10-04T21:46:48"
- }
---
## Method: DELETE
#### /api/v1/meetup/delete/{id}
##### PathVariable:
1) id - Long (1...Long.MAX_VALUE)
