# serializationComparator
Just to compare different serializator libraries performances

## What I do?

1. Generate an XML file with a customisable number of entries
2. Generate a JSON file with the same structure and same entries
3. Convert the JSON into a FlatBuffer binary using `flatc`
4. Serialize both and access the whole data set
5. Print out the results

## Before running the program
You need to install `flatc`. 

Either try to execute `brew install flatc` if you are on Mac or check the documentation here: http://google.github.io/flatbuffers/flatbuffers_guide_building.html
