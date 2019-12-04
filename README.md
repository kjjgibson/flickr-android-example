# Flickr Android Example

This app allows users to search for images from Flickr. It displays an infinitely scrolling list of images that match the search term.

## Design Decisions

The app follows MVVM. My main goal was to follow the principle of separation of concerns. 

The layers of the app are: UI (Activity, fragment), ViewModel, Repository, Model/Remote data source.
Each layer only has a connection with the layer below it - the UI knows nothing about the Repository for example.

The UI layer is as thin as possible and is solely responsible for binding data to the view. This makes it much easier to test as we can reduce our reliance on difficult Activity and Fragment tests.

My second goal was to have a smooth user experience. The app is driven largely by the data instead of the UI. The flow of data always goes from the DB to the views. 
When new data is received it's persisted and propagated to the views so that it's never lost when there are configuration changes, app exits, or network issues.

Lastly I used Android's architecture components to provide a clean architecture and adhere to their guidelines and best practices.

## Trade-offs

Given that I'm not using third party libraries I implemented a ServiceLocator pattern. Normally I would use dependency injection with Dagger to simplify the dependency graph and more importantly make testing easier.

I would also normally use GSON for JSON deserialization, Retrofit for performing network requests, and Glide to load/cache the images. My own implementations are obviously no where near as battle tested or feature rich as these libraries. 

## Future Improvements

* Caching the images would make a huge difference. Ideally a combination of an in-memory cache as well as a file cache would be best. It may also be worth scaling the images
* Espresso tests to test the whole flow
* Placeholder images in the PagedList when fetching the next page of images from the network (could remove the "Loading..." text and make it more seamless)
* Handle network connectivity better (could open network settings if there is no network turned on for example - or at least provide better notifications)
* Link to a specific image so that you can see it larger and also the details (date, full title, etc.)
* Save the most recent searches to make it easier to see what you've searched and search for it again
* Show new images when you first load the app so that it's not super empty before you search
* Design improvements (I haven't given this much love so far)
* Pull to refresh
* Share images to social media
* Download images to device/camera
* Share images from device/camera
* Dozens of other features that a real production app would have :)