package com.shwetank.libraryassistant.network;

import android.content.Context;
import android.os.AsyncTask;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.shwetank.libraryassistant.ArtData;
import com.shwetank.libraryassistant.ArtistData;
import com.shwetank.libraryassistant.model.Art;
import com.shwetank.libraryassistant.model.Artist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManagerImpl implements NetworkManager {

    private Retrofit mRetrofit;
    private String BASE_URL = "https://www.google.com";
    private static NetworkManagerImpl INSTANCE;

    private NetworkManagerImpl(Context context) {
        if (mRetrofit == null) {
            mRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static NetworkManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManagerImpl(context);
        }
        return INSTANCE;
    }

    @Override
    public void getBulkArtData(List<String> beaconList, ArtData artData) {
        ArtDataAsyncTask asyncTask = new ArtDataAsyncTask(beaconList, artData);
        asyncTask.execute();
    }

    @Override
    public void getArtistInformation(String artistName, ArtistData artistData) {
        ArtistDataAsyncTask artistDataAsyncTask = new ArtistDataAsyncTask(artistName, artistData);
        artistDataAsyncTask.execute();
    }

    @Override
    public void getRecommendedArt(Art art, ArtData artData) {
        RecommendedDataAsyncTask recommendedDataAsyncTask = new RecommendedDataAsyncTask(art, artData);
        recommendedDataAsyncTask.execute();
    }

    class ArtDataAsyncTask extends AsyncTask<Void, Void, List> {


        private final List<String> beaconList;
        private final ArtData artData;

        public ArtDataAsyncTask(List<String> beaconList, ArtData artData) {
            this.beaconList = beaconList;
            this.artData = artData;
        }

        @Override
        protected List<Art> doInBackground(Void... voids) {
            List<Art> artList = new ArrayList<>();
            for (String s : beaconList) {
                String query = "PREFIX cidoccrm: <http://www.cidoc-crm.org/cidoc-crm/>\n" +
                        "PREFIX saam: <http://edan.si.edu/saam/id/ontologies/>\n" +
                        "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX ds2: <http://54.153.4.119:3030/ds2/>\n" +
                        "PREFIX ds3: <http://54.153.122.152:3030/ds3/>\n" +
                        "SELECT ?artwork ?artworkurl ?artworkname ?artworkinfo ?artist ?artistfirstname ?artistlastname ?artisturl ?artistbio ?artistnationality ?artistbirthdatelabel ?artistdeathdatelabel ?artworktype ?artworkclassification ?artworkclassification2 ?currentkeeper ?artworkmedium\n" +
                        "WHERE {\n" +
                        "  ?location ?label \"" + s + "\".\n" +
                        "  ?artwork cidoccrm:P55_has_current_location ?location .\n" +
                        "  \n" +
                        "  SERVICE ds2:sparql { \n" +
                        "    ?artwork cidoccrm:P138i_has_representation ?artworkurl .\n" +
                        "  \t\n" +
                        "    ?artwork cidoccrm:P102_has_title ?artworktitle .\n" +
                        "  \t?artworktitle rdfs:label ?artworkname .\n" +
                        "    \n" +
                        "    ?artwork saam:PE_has_note_gallerylabel ?artworkinfo .\n" +
                        "  \t?producedby cidoccrm:P108_has_produced ?artwork .\n" +
                        "    \n" +
                        "    ?producedby cidoccrm:P14_carried_out_by ?artist .\n" +
                        "    \n" +
                        "  \t?artwork rdf:type ?artworktype .\n" +
                        "  \t?artwork saam:PE_object_mainclass ?artworkclassification .\n" +
                        "  \tOPTIONAL { ?artwork saam:PE_object_subsclass ?artworkclassification2 . }\n" +
                        "  \t?artwork cidoccrm:P50_has_current_keeper ?currentkeeper .\n" +
                        "  \t?artwork saam:PE_medium_description ?artworkmedium .\n" +
                        "  }\n" +
                        "  SERVICE ds3:query {\n" +
                        "  \t?artist saam:PE_has_note_artistbio ?artistbio .\n" +
                        "    \n" +
                        "    ?artist cidoccrm:P107i_is_current_or_former_member_of ?artistnatiionalityobj .\n" +
                        "  \t?artistnatiionalityobj skos:prefLabel ?artistnationality .\n" +
                        "  \n" +
                        "  \t?artist cidoccrm:P98i_was_born ?artistbirthdateobj .\n" +
                        "  \t?artistbirthdateobj cidoccrm:P4_has_time-span ?artistbirthdate .\n" +
                        "  \t?artistbirthdate cidoccrm:P82_at_some_time_within ?artistbirthdatelabel .\n" +
                        " \n" +
                        "  \t?artist cidoccrm:P100i_died_in ?artistdeathdateobj .\n" +
                        "  \t?artistdeathdateobj cidoccrm:P4_has_time-span ?artistdeathdate .\n" +
                        "  \t?artistdeathdate cidoccrm:P82_at_some_time_within ?artistdeathdatelabel .\n" +
                        "  \n" +
                        "  \t?artist cidoccrm:P1_is_identified_by ?displayname .\n" +
                        "    \t?displayname saam:PE_firstname ?artistfirstname .\n" +
                        "  \t?displayname saam:PE_lastname ?artistlastname .\n" +
                        "  \n" +
                        "    \tOPTIONAL { ?artist cidoccrm:P138i_has_representation ?artisturl . }\n" +
                        "  }\n" +
                        "  FILTER (contains(str(?displayname), \"displayname\"))\n" +
                        "  FILTER NOT EXISTS {\n" +
                        "  \tFILTER (contains(str(?artworkurl), \"jpg\"))\n" +
                        "  }\n" +
                        "}\n" +
                        "LIMIT 1";
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://ec2-54-193-127-210.us-west-1.compute.amazonaws.com:3030/ds1/query", query);
                ResultSet resultSet = queryExecution.execSelect();
                QuerySolution querySolution = resultSet.next();

                Art art = new Art();
                art.setArtistDeathDate(querySolution.get("artistdeathdatelabel").toString());
                art.setArtistImageUrl(querySolution.get("artisturl").toString());
                art.setArtWorkImageUrl(querySolution.get("artworkurl").toString());
                art.setArtWorkMedium(querySolution.get("artworkmedium").toString());
                art.setArtWorkDescription(querySolution.get("artworkinfo").toString());
                art.setArtistBirthDate(querySolution.get("artistbirthdatelabel").toString());
                art.setArtistName(querySolution.get("artistfirstname").toString().concat(" ").concat(querySolution.get("artistlastname").toString()));
                art.setArtWorkType(querySolution.get("artworktype").toString());
                art.setArtistBio(querySolution.get("artistbio").toString());
                art.setArtWorkClassification(querySolution.get("artworkclassification").toString());
                art.setCurrentKeeper(querySolution.get("currentkeeper").toString());
                art.setArtWorkName(querySolution.get("artworkname").toString());
                art.setArtistNationality(querySolution.get("artistnationality").toString());
                artList.add(art);
            }
            return artList;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            artData.artData(list);
        }
    }

    class RecommendedDataAsyncTask extends AsyncTask<Void, Void, List> {
        private Art art;
        private final ArtData artData;

        public RecommendedDataAsyncTask(Art art, ArtData artData) {
            this.art = art;
            this.artData = artData;
        }

        @Override
        protected List<Art> doInBackground(Void... voids) {
            List<Art> artList = new ArrayList<>();
            String query = "PREFIX cidoccrm: <http://www.cidoc-crm.org/cidoc-crm/>\n" +
                    "PREFIX saam: <http://edan.si.edu/saam/id/ontologies/>\n" +
                    "PREFIX thesauri: <http://edan.si.edu/saam/id/thesauri/classification/>\n" +
                    "PREFIX museum: <http://edan.si.edu/saam/id/>\n" +
                    "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX ds2: <http://54.153.4.119:3030/ds2/>\n" +
                    "PREFIX ds3: <http://54.153.122.152:3030/ds3/>\n" +
                    "\n" +
                    "SELECT ?artwork \n" +
                    "(SAMPLE(?artworkurl) AS ?recommendedartworkurl) \n" +
                    "(SAMPLE(?artworkname) AS ?recommendedartworkname) \n" +
                    "(SAMPLE(?artworkinfo) AS ?recommendedartworkinfo)\n" +
                    "(SAMPLE(?artist) AS ?recommendedartist) \n" +
                    "(SAMPLE(?artistfirstname) AS ?recommendedartistfirstname) \n" +
                    "(SAMPLE(?artistlastname) AS ?recommendedartistlastname) \n" +
                    "(SAMPLE(?artisturl) AS ?recommendedartisturl) \n" +
                    "(SAMPLE(?artistbio) AS ?recommendedartistbio) \n" +
                    "(SAMPLE(?artistnationality) AS ?recommendedartistnationality) \n" +
                    "(SAMPLE(?artistbirthdatelabel) AS ?recommendedartistbirthdatelabel) \n" +
                    "(SAMPLE(?artistdeathdatelabel) AS ?recommendedartistdeathdatelabel)\n" +
                    "WHERE {\n" +
                    "  SERVICE ds2:sparql {\n" +
                    "  \t?artwork rdf:type <" + art.getArtworkType() + ">.\n" +
                    "  \t?artwork saam:PE_object_mainclass <"+art.getArtworkClassification()+"> .\n" +
                    "  \t?artwork cidoccrm:P50_has_current_keeper <"+art.getCurrentKeeper()+"> .\n" +
                    "  \t?artwork saam:PE_medium_description \""+art.getArtworkMedium()+"\" .\n" +
                    "  \n" +
                    "  \n" +
                    "  \t?artwork cidoccrm:P138i_has_representation ?artworkurl .\n" +
                    "  \n" +
                    "  \t?artwork cidoccrm:P102_has_title ?artworktitle .\n" +
                    "  \t?artworktitle rdfs:label ?artworkname .\n" +
                    "  \n" +
                    "  \t?artwork saam:PE_has_note_gallerylabel ?artworkinfo .\n" +
                    "  \t?producedby cidoccrm:P108_has_produced ?artwork .\n" +
                    "  \n" +
                    "  \t?producedby cidoccrm:P14_carried_out_by ?artist .\n" +
                    "  }\n" +
                    "  SERVICE ds3:query {\n" +
                    "    \t?artist saam:PE_has_note_artistbio ?artistbio .\n" +
                    "  \n" +
                    "  \t?artist cidoccrm:P107i_is_current_or_former_member_of ?artistnatiionalityobj .\n" +
                    "  \t?artistnatiionalityobj skos:prefLabel ?artistnationality .\n" +
                    "  \n" +
                    "  \t?artist cidoccrm:P98i_was_born ?artistbirthdateobj .\n" +
                    "  \t?artistbirthdateobj cidoccrm:P4_has_time-span ?artistbirthdate .\n" +
                    "  \t?artistbirthdate cidoccrm:P82_at_some_time_within ?artistbirthdatelabel .\n" +
                    "  \n" +
                    "  \t?artist cidoccrm:P100i_died_in ?artistdeathdateobj .\n" +
                    "  \t?artistdeathdateobj cidoccrm:P4_has_time-span ?artistdeathdate .\n" +
                    "  \t?artistdeathdate cidoccrm:P82_at_some_time_within ?artistdeathdatelabel .\n" +
                    "  \n" +
                    "  \t?artist cidoccrm:P1_is_identified_by ?displayname .\n" +
                    "  \t?displayname saam:PE_firstname ?artistfirstname .\n" +
                    "  \t?displayname saam:PE_lastname ?artistlastname .\n" +
                    "  \n" +
                    "  \tOPTIONAL { ?artist cidoccrm:P138i_has_representation ?artisturl . }\n" +
                    "  }\n" +
                    "  FILTER (contains(str(?displayname), \"displayname\"))\n" +
                    "  FILTER NOT EXISTS {\n" +
                    "  \tFILTER (contains(str(?artworkurl), \"jpg\"))\n" +
                    "  }\n" +
                    "}\n" +
                    "GROUP BY ?artwork\n" +
                    "LIMIT 5";
            QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://ec2-54-193-127-210.us-west-1.compute.amazonaws.com:3030/ds1/query", query);
            ResultSet resultSet = queryExecution.execSelect();
            while (resultSet.hasNext()) {
                QuerySolution querySolution = resultSet.next();
                Art art = new Art();
                art.setArtWorkImageUrl(querySolution.get("recommendedartworkurl").toString());
                art.setArtistName(querySolution.get("recommendedartistfirstname").toString().concat(" ").concat(querySolution.get("recommendedartistlastname").toString()));
                art.setArtWorkName(querySolution.get("recommendedartworkname").toString());
                artList.add(art);
            }
            return artList;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            artData.artData(list);
        }
    }


    class ArtistDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String artistName;
        private final ArtistData artistData;

        public ArtistDataAsyncTask(String artistName, ArtistData artistData) {
            this.artistName = artistName;
            this.artistData = artistData;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mRetrofit.create(GetDataService.class).getArtist(artistName).enqueue(new Callback<Artist>() {
                @Override
                public void onResponse(Call<Artist> call, Response<Artist> response) {
                    sendData();
                }

                @Override
                public void onFailure(Call<Artist> call, Throwable t) {
                    sendData();
                }
            });
            return null;
        }

        public void sendData() {
            Artist artist = new Artist();
            artist.setBirthDate(new Date().toString().substring(0, 10));
            artist.setNationality("American");
            artist.setArtistDescription("Frank Blackwell Mayer was born into a life of privilege. His father was a lawyer who represented Baltimore in the Maryland State Senate, and his mother was the daughter of a merchant ship captain. Mayer established the Artists Association of Maryland and remained active in Baltimore’s art community throughout his career. He lived in Paris during the Civil War, returning home to discover that the war had destroyed the city’s art community and sapped his family’s finances. In December 1876 he bought a house in Annapolis and hung a sign above the door bearing the motto: “He lives best who lives well in obscurity.” (Page, “Francis Blackwell Mayer,” <i>The Magazine Antiques</i>, February 1976)");
            artist.setImageUrl("https://2.americanart.si.edu/images/luce/artists/portrait_image_113230.jpg");
            artist.setArtistName(artistName);
            artist.setBirthDate("1827/12/27");
            artistData.artistData(artist);
        }
    }
}
