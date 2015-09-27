# Reagent Pagination Example

This is just a very simple example of how to implement pagination using reagent and
bootstrap. .The idea is to create a data structure which consists of hash maps where
each key is a page number and the value is a vector of records or items to be
displayed on that page. In the example, a temporary data structure is used with dummy
values. The pager component takes a sorted list of the page numbers and renders them
as a pagination list along the top of the page. When you click on one of the page
links, the records corresponding to that page are displayed.

The principal is pretty simple. The component maintains a :pager-current-page value
in the state atom. When you click on one of the pages, this value is updated with the
corresponding number of the page. Another render function then renders the records
from the hash map which correspond to that index. 

See the `data` var to see how records can easily be partitioned and an indexed map
build to use for the pager component.

## Todo

Really should create a records per page i.e. [;pager :page-size] value in the state
and use that to partition the records up into pages for display. You could then have
a simple input component which would allow the user to set the number of records per
page.

Need to implement a way to allow for large numbers of pages such that only a subset
are shown at once and the numbers slide to show subsets. Currently, if you have more
pages than can fit on the width of the page, they will wrap and create multiple lines
of pages - works fine, but is pretty ugly. An easy way to do this would be to move
the data structure down another level, so that you have an outer level of page
*windows*, where each *window* contains a list of the hash maps to be displayed in
that window. You could then use a [:pager :window] value in the session state to
track which window you are displaying. When you try to move to previous or next and
you are at the first or last item in that window, it will decrement/increment the
window and display the indexes from that window.  

