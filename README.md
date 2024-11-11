# rest-example
Al llarg del mini 1 s'han creat els objectes PuntoInteres, Usuario i Registro. Seguint amb el enunciat s'ha creat també una interfície amb les funcions propies (definides abaix)  que seran sobreescrites en la classe GameManagerImpl. 
    void addUser(String id, String name, String surname, String fechaNacimiento); --> afegeix un usuari al hashmap d'usuaris
    List<Usuario> getUsersOrderedAlphabetically(); --> ordena en ordre alfabètic, és de tipus llista, agafa tots els elements de Hashmap de usuari i els posa en una llista per filtrar per ordre alfabètic
    Usuario getUser(String id); -->ens dona la informació d'un usuari concret amb un cert id, degut a això el llistat inicial de usuaris es un Hashmap
    void addPointOfInterest(int x, int y, ElementType type); --> afegeix els punts d'interès al hash set de punts ja que aquests no es poden repetir.
    void registerPointVisit(String userId, int x, int y) throws EmptyPointListException; --> Implementa la excepció descrita si no hi ha punt o bé si no existeix el id
    List<PuntoInteres> getUserVisitedPoints(String userId);-->ens dona els punts visitats per l'usuari
    List<Usuario> getUsersByPoint(int x, int y) throws EmptyPointListException;--> ens dona els usuaris per punt
    List<PuntoInteres> getPointsByType(ElementType type);--> ens dona els punts per tipus

    Finalment, el test de Junit m'ha fallat en la funció puntosInteresElementType(), apart d'això he iniciat el GameService, el qual ens ajuda a 
