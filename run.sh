#!/usr/bin/env bash

set -e

day=$(printf "%02d" "$1")

kotlinc -script "src/day$day.kts" < "data/input$day.txt"
