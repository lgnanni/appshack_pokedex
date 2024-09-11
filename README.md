# Appshack_pokedex

Technical task to consume the PokeApi for the making of a simple Pokedex App

- Used Jetpack Compose for development
- Used Retrofit2 for API consumption
- Used Room for local database handling
- Used repository pattern to manage between local and remote data
- Used MvvM pattern
- Used Hilt for DI
- Used Coil for image management

  Due to the time and complexities of navigating the API there are things that were left incomplete/TODO, which are:

- A better more expressive UI for giving information about the pokemons evolution condition
- Error handling, there's a basic abstract implementation done in the app but not connected to handle potential errors
- On this there's also internet connection awareness which has not been connected to any handling
- Unit testings
