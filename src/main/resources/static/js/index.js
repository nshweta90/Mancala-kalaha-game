function loadSavedGameList()
{
    document.getElementById('loadgameform').innerHTML='';
    fetch('http://localhost:8080/getAllGames', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
       
    }).then((res) => res.json()).then((data) => { 
         console.log(data);
        if(data!=null && data.length>0)
        {
            
           let output='';
          
            data.forEach(element => {
                output+='<option value="'+element.id+'">'+element.id +'</option>';
            });
            let strdata ='<select class="formelement" id="gameid" name="gameid">'+output+
            '</select><input class="buttonelement load-game" type="submit" value="Load Saved Game" onclick="validate()" />'
            document.getElementById('loadgameform').innerHTML+=strdata;
        }
        }).catch((error) => {
            console.log(error);
        });
}
loadSavedGameList();


