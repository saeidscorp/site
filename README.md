# Site

*(My personal site/blog system written in Clojure)*
 [![Build Status](https://travis-ci.org/saeidscorp/site.svg?branch=master)](https://travis-ci.org/saeidscorp/site)

## Installation

You have to clone the github repository (since it doesn't make sense to
put it on clojars)

## Usage

### Migrate the database

You have to run database migrations first:

    lein migrate

Then you also probably want to feed the database some initial data:

    lein seed

Running the above command will insert rows defined in `env/dev/seeds/...` files
into database.

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

And if you don't want the extra overhead of a whole JVM running for
compiling Sass files, you can also run:

    site.user => (oneshot)

or

    site.user => (watch)

## License

Copyright © 2015 Saeid Akbari (scorp.one)

Distributed under the GNU GENERAL PUBLIC LICENSE Version 3
