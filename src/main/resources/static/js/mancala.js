 

function createNewGame() {
    const params = new URLSearchParams(document.location.search);
    const player1 = params.get("pname1");
    const player2 = params.get("pname2");
    console.info(player1);
    console.info(player2);

    fetch('http://localhost:8080/startGame', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify([{
            "playerName": player1
        }, {
            "playerName": player2
        }])
    }).then((res) => res.json()).then((data) => {
        validateAndLoad(data);

        }).catch((error) => {
            console.log(error);
        });
}

checkAndCreateGame();

function checkAndCreateGame(){
    var param = new URLSearchParams(document.location.search);
    var gameToLoad =param.get("gameid");
    if(gameToLoad!=null && gameToLoad>0){
        loadSavedGame(gameToLoad);
    }else{         
         createNewGame();
    }
} 

 
function loadSavedGame(gameToLoad){

  
    fetch('http://localhost:8080/getGame/'+gameToLoad, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        } 
    }).then((res) => res.json()).then((data) => {
        validateAndLoad(data);

        }).catch((error) => {
            console.log(error);
        });

}




function validateAndLoad(data) {
    if (data.message != null) {
        alert(data.message);

    }
    else if (data.id != null && data.playerList != null) {

        refreshBoardWithGameData(data);

    }
}

function refreshBoardWithGameData(data) {
 
    console.log(data);
   
    document.getElementById('mainBoard').innerHTML='';
    document.getElementById('p0board').innerHTML= '';
    document.getElementById('p1board').innerHTML= '';
    document.getElementById('gameidlocation').innerHTML= 'Game Id : '+data.id;

    var count = 0;
    data.playerList.forEach(element => {

        if(data.activePlayerID == element.id){  
            document.getElementById('gameidlocation').innerHTML+= ' ---- Active Player : '+element.playerName;
        }


        let myArray = new Array();
        element.pitList.forEach(e => {
            myArray.push('<div  class="pit"   onclick="playMove(this)" gameID="' + data.id + '" player_id="' + element.id + '"     pit_idx="' + e.id + '" value="' + e.stoneCount + '">' + e.stoneCount + ' </div>');
        });

        if (count == 0) {
            document.getElementById('p1name').innerHTML = '<h2>' + element.playerName + '</h2>';
            document.getElementById('p0board').innerHTML += '<div   class="pit" id="' + element.bigPit.id + '">' + element.bigPit.stoneCount + '</div>';
            myArray.reverse();
            console.log('reversing array');
        }
        let output = '';
        myArray.forEach(arrayelement => {
            output += arrayelement;
        });
        document.getElementById('mainBoard').innerHTML += '</br><div class="midboard" id="' + element.id + '">' + output + '</div>';

        if (count != 0) {
            document.getElementById('p1board').innerHTML += '<div   class="pit" id="' + element.bigPit.id + '">' + element.bigPit.stoneCount + '</div>';
            document.getElementById('p2name').innerHTML = '<h2>' + element.playerName + '</h2>';;
        }
        count++;
    });
}

function playMove(item) {
    var pit = item.getAttribute("pit_idx");
    var player = item.getAttribute("player_id");
    var gameId = item.getAttribute("gameID");

    fetch('http://localhost:8080/playGameMove/' + gameId, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            {
                "playerID": player,
                "pitIndex": pit
            }
        )
    }).then((res) => res.json()).then((data) => {
        validateAndLoad(data);

    }).catch((error) => {
        console.log(error);
    });



}

