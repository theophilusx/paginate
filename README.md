# Reagent Pagination Example

This is just a very simple example of how to implement pagination using reagent and
bootstrap. .The idea is to create a data structure which consists of hash maps where
each key is a page number and the value is a vector of records or items to be
displayed on that page. To handle more pages than would fit in one index line, a
further data structure consisting of an index of page indexes is used. 

In the example, the function `init-pager` is used to setup the necessary data
structures and state information used by this simple component. The current index and
current page are stored in the session state atom. When the user clicks on an index,
it sets the current index. This value is used to lookup the records in the data index
to display on that page. The previous and next links move to the previous or next
index. This could involve moving to the previous or next page of indexes.

the pager-init function takes a page size and index size argument. The page size
determines how many records are to be displayed on each page. This also determine the
number of indexes which will exist i.e. total number of records / page size. The
index size determines home many page indexes are displayed in the index bar at the
top.

The page-init also accepts a collection which represents the collection of
records. It will build the necessary indexes from this collection. 

The principal is pretty simple. The component maintains a :current-index value in the
state atom. When you click on one of the pages indexes, this value is updated with
the corresponding number of the page. Another component then renders the
records from the data list which correspond to that index page.

## Warning

This was really just an experiment I used to understand Reagent a little better. In
reality, you could probably use one of the existing React pagination components to do
this. However, I haven't progressed that far yet! This one got the job done for
now. use at your own risk!
