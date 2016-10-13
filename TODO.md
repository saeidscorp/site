## Project TODOs

### Important
- [ ] Blog admin section needs authentication
- [ ] Make comments available via Ajax
- [ ] Users need registration and login
- [ ] Port the entire template to Semantic-UI (removing bootstrap and changing predefined styles accordingly)
    - [x] Customize template to better match my usage
    - [ ] Redesign the layout
- [ ] Implement search capability
- [ ] Multi-lingual site
- [ ] A full-fledged style for Markdown (consistent across preview and real view)
- [ ] Make sure all backend services (like DB) use UTC time
- [ ] Automated CI/deployment by Travis OpenShift Deployment
    - [ ] Using binary deployments to reduce server down-time
- [x] Utilize a build tool for CSS/JS minifying
    - [x] Strip down all assets and eliminate redundancy
    - [x] Remove previous big files from git repo
- [ ] Post edit/delete capability

### Extra
- [x] Pretty dates (such as "July 2, 2016")
- [ ] Profile the handlers (with clojure-mini-profiler)
- [ ] Add external rewrite rules file (using `ring-rewrite`)
- [ ] Add CloudFlare CDN support to the project
    - [x] Remove all library files from project 
- [ ] Add page caching
- [x] Try using an asset pipeline (such as `optimus`)
- [ ] Design another favicon
- [ ] Another Logo
- [ ] Subscription & RSS feeds
- [ ] Footnotes/Highlights support in Markdown rendering engine
- [ ] Image placeholders support in Markdown editor
- [ ] Image thumbnails anywhere needed