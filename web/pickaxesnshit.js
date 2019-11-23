function constructBlock(transactions) {
    var trans_json = JSON.parse(transactions);
    var block_json = {};
    block_json.data = transactions;
    block_json.transactions = trans_json.length;
    $.get( "/web/block", function (data) {
        var blocks = JSON.parse(data);
        block_json.prevHash = blocks[blocks.length - 1].hash;
        block_json.proofOfWork = mine(block_json);
        console.log("Block mined with: " + block_json.proofOfWork);
        submitBlock(block_json);
    });
}

function submitBlock(block_json) {
    $.post( "/web/block",
        {
            block: JSON.stringify(block_json)
        }, function (data) {
            console.log(data);
    });
}

$(document).ready ( function () {
    $('#submit').click(function() {
        $.get( "/web/transaction/count/50", function (data) {
            console.log(data);
            constructBlock(data);
        });
    });
});

var getProofOfWork = function (test) {
    var hash = new Int8Array(8);
    for ( var index = hash.length - 1; index >= 0; --index) {
        var byte = test & 0xff;
        hash [ index ] = byte;
        test = (test - byte) / 256 ;
    }
    return hash;
};

function verifyProofOfWork(blockHash) {
    //console.log(blockHash);
    var difficulty = 3;
    for (var i = 0; i < difficulty; i++) {
        if (blockHash[i] !== '0')
            return false;
    }
    return true;
}

function concatArrays(a, b, c) {
    var d = new Int8Array(a.length + b.length + c.length);
    d.set(a);
    d.set(b, a.length);
    d.set(c, a.length + b.length);
    return d;
}

function str2ab(str) {
    var strLength = str.length;
    var ab = new Int8Array(strLength);
    for (var i = 0; i < strLength; i++) {
        ab[i] = str.charCodeAt(i);
    }
    return ab;
}
function arrayBufferToWordArray(ab) {
    var i8a = new Uint8Array(ab);
    var a = [];
    for (var i = 0; i < i8a.length; i += 4) {
        a.push(i8a[i] << 24 | i8a[i + 1] << 16 | i8a[i + 2] << 8 | i8a[i + 3]);
    }
    return CryptoJS.lib.WordArray.create(a, i8a.length);
}

function mine(block) {
    var data = str2ab(block.data);
    for (var i = 0; true; i++) {
        var wordArray = arrayBufferToWordArray(concatArrays(data, block.prevHash, getProofOfWork(i)));
        var hash = CryptoJS.SHA256(wordArray);
        if (verifyProofOfWork(hash.toString(CryptoJS.enc.Hex)))
            return i;
    }
}