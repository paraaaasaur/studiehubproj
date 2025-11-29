## Domain Issues
- `mimeTypePic`, `mimeTypeAudio`:
  1. merely an indirect check on db media existence in representation layer
  2. dependency for building base64 to feed HTTP
  3. relying on `ServletContext`
  4. remarks: 
     - noise columns only to support db lobs 
     - completely redundant when using storage
- Magic getters `getQ_pictureString`, `getQ_audioString`
- Magic toString


## Nick's Wishlist (Probably)

### jp-ms-questions-exam
- [x] goto: `GET /question.controller/tstartRandomExam` to `question/examMultipleQuestion`
- [ ] get-data:
- [ ] UI accessibility
- [ ] view is completed

### jp-random-questions-exam
- [x] goto: `GET /question.controller/startRandomExam` to `question/examQuestion`
- [x] get-data: `GET /question.controller/sendRandomExam`　`GET /exam/jp/mix`
- [ ] UI accessibility
- [ ] view is completed

### jp-mixed-questions-exam
- [x] goto: `GET /question.controller/startRandomMixExam` to `question/examMixQuestion`
- [x] get-data: `GET /question.controller/sendRandomMixExam`
- [x] UI visibility
- [x] view is completed

### jp-ss-questions-exam
- [ ] goto:
- [ ] get-data:
- [ ] UI accessibility
- [ ] view is completed

### en-mixed-questions-exam