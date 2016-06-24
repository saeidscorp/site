# Site (My personal site/blog system written in Clojure) [![Build Status](https://travis-ci.org/saeidscorp/site.svg?branch=master)](https://travis-ci.org/saeidscorp/site)



## Installation

You have to clone the github repository (since it doesn't make sense to
put it on clojars)

## Usage

### Migrate the database

You have to run database migrations first:

    lein migrate

### Running

Just run it:

    lein run

Or start the repl and start the system:

    lein repl
    
    site.user => (go)


## Notes

I have used Sass preprocessor to generate css files.
You can compile the scss files in one shot using:

    lein sass once

Or watch the directories and auto build them:

    lein sass watch

## License

Copyright © 2015 Saeid Akbari (scorp.one)

Distributed under the GNU GENERAL PUBLIC LICENSE Version 3
