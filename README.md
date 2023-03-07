# eng1420-project

## TODO

- [ ] `Entry`
  - [ ] `RemoteEntry extends Entry`
  - [ ] `LocalEntry extends Entry`
    - We may or may not know a `RemoteEntry`'s name or if it is a directory.
    - `static` members for Laserfiche account ID/keys
- [ ] `ProcessingElement`
  - Local vs Remote
  - [ ] `Filter extends ProcessingElement`
    - [ ] `NameFilter extends Filter`
    - [ ] `LengthFilter extends Filter`
    - [ ] `ContentFilter extends Filter`
    - [ ] `CountFilter extends Filter`
  - [ ] `Split extends ProcessingElement`
  - [ ] `ListFiles extends ProcessingElement` (called List in description) 
  - [ ] `Rename extends ProcessingElement`
  - [ ] `Print extends ProcessingElement`
- [ ] Other
  - [ ] Scenario Parser
  - [ ] Scenario Executor
  - [ ] `Main`
  - [x] This README

## Optional multithreading
  - [ ] Proces input entries in parallel
  - [ ] Run `ProcessingElement`s in parallel
  - [ ] Send entry metadata to later `ProcessingElement`s before sending content
  - [ ] Stream entry content to later `ProcessingElement`s
  - [ ] Use stream-optimized algorithms for searching in content.
