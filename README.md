# engg1420-project

## TODO

- [x] `Entry`
  - [x] `RemoteEntry extends Entry`
  - [x] `LocalEntry extends Entry`
    - [x] We may or may not know a `RemoteEntry`'s name or if it is a directory.
    - [x] `static` members for Laserfiche account ID/keys
- [x] `ProcessingElement`
  - [X] Local vs Remote
  - [ ] `Filter extends ProcessingElement`
    - [ ] `NameFilter extends Filter`
    - [ ] `LengthFilter extends Filter`
    - [ ] `ContentFilter extends Filter`
    - [ ] `CountFilter extends Filter`
  - [ ] `Split extends ProcessingElement`
  - [ ] `ListFiles extends ProcessingElement` (called List in description) 
  - [ ] `Rename extends ProcessingElement`
  - [ ] `Print extends ProcessingElement`
- [x] Other
  - [x] Scenario Parser
  - [x] Scenario Executor
  - [x] `Main`
  - [x] This README

## Optional multithreading
  - [x] Proces input entries in parallel
  - [ ] Run `ProcessingElement`s in parallel
