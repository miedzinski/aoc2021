#!/usr/bin/env bash

kotlinc -script "src/day$1.kts" < "data/input$1.txt"
