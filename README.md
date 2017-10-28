# clj-2048-ai

![](/img/2048-clj/demo.gif)
![](https://raw.githubusercontent.com/jerben/clj-2048-ai/master/demo.gif)


You can read more about the [bot](https://gfycat.com/gifs/detail/HighThornyBlesbok) in this blog post. (TODO)

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server on `localhost:3000` for the bot, run:

    lein ring server

## Usage

Simply `POST` the game board you want the best move for to `http://localhost:3000`.

Request:

```
POST / HTTP/1.0
Content-Type: application/json

[[16 2 2 0] [8 2 0 0] [4 2 2 0] [0 0 0 2]]
```

Response:

```
2
```

This is the mapping of int to move:

```clojure
(def moves-map {:up 0 :down 1 :left 2 :right 3})
```
