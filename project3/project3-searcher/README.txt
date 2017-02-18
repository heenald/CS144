
Task B.2
We created the table item_category with following two columns
item_id
location which is a Point(latitude, longitude)

and the spatial index is created on location column

Task B.3
The spatialSearch method uses the basicSearch method to get all items in the DB and then uses spatial index to identify item ids that belong to the given region, finally calculating a intersection of two results. Thus the order of results stays same as in the basicSearch.

If the number of results search finds is lesser than numResultsToReturn - numResultsToSkip, it returns an array of that size only (instead of returning an array of size numResultsToReturn and leaving some elements null).

Task B.4
Escaping characters: We observed that quote (“) and apostrophe (‘) were part of description as it is and not escaped in original eBay data XMLs. Hence we have not escaped them either.
We’ve escaped &, < and > in Description, Name, Location and Category since those are the only tags where we found these characters.

