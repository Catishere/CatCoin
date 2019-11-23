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
    hash[7] = test  &   0x00000000000000FF;
    hash[6] = (test &   0x000000000000FF00) >> 8;
    hash[5] = (test &   0x0000000000FF0000) >> 16;
    hash[4] = (test &   0x00000000FF000000) >> 24;
    hash[3] = (test &   0x000000FF00000000) >> 32;
    hash[2] = (test &   0x0000FF0000000000) >> 40;
    hash[1] = (test &   0x00FF000000000000) >> 48;
    hash[0] = (test &   0xFF00000000000000) >> 56;
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

function concatArray(a, b) {
    var c = new Int8Array(a.length + b.length);
    c.set(a);
    c.set(b, a.length);
    return c;
}

function mine(block) {
    var utf8Encode = new TextEncoder();
    var utf8Decode = new TextDecoder();
    var data = utf8Encode.encode(block.data);
    for (var i = 0; true; i++) {
        var h = utf8Decode.decode(concatArray(data, concatArray(block.prevHash, getProofOfWork(i))));
        if (verifyProofOfWork(SHA256(h)))
            return i;
    }
}

function SHA256(s) {
    var chrsz   = 8;
    var hexcase = 0;
    function safe_add (x, y) {
        var lsw = (x & 0xFFFF) + (y & 0xFFFF);
        var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
        return (msw << 16) | (lsw & 0xFFFF);
    }

    /**
     * @return {number}
     */
    function S (X, n) { return ( X >>> n ) | (X << (32 - n)); }
    function R (X, n) { return ( X >>> n ); }
    /**
     * @return {number}
     */
    function Ch(x, y, z) { return ((x & y) ^ ((~x) & z)); }
    /**
     * @return {number}
     */
    function Maj(x, y, z) { return ((x & y) ^ (x & z) ^ (y & z)); }
    /**
     * @return {number}
     */
    function Sigma0256(x) { return (S(x, 2) ^ S(x, 13) ^ S(x, 22)); }
    /**
     * @return {number}
     */
    function Sigma1256(x) { return (S(x, 6) ^ S(x, 11) ^ S(x, 25)); }
    /**
     * @return {number}
     */
    function Gamma0256(x) { return (S(x, 7) ^ S(x, 18) ^ R(x, 3)); }
    /**
     * @return {number}
     */
    function Gamma1256(x) { return (S(x, 17) ^ S(x, 19) ^ R(x, 10)); }
    function core_sha256 (m, l) {
        var K = [0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1, 0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3, 0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786, 0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA, 0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, 0xC6E00BF3, 0xD5A79147, 0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13, 0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B, 0xC24B8B70, 0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070, 0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208, 0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2];
        var HASH = [0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19];
        var W = new Array(64);
        var a, b, c, d, e, f, g, h;
        var T1, T2;
        m[l >> 5] |= 0x80 << (24 - l % 32);
        m[((l + 64 >> 9) << 4) + 15] = l;
        for ( var i = 0; i<m.length; i+=16 ) {
            a = HASH[0];
            b = HASH[1];
            c = HASH[2];
            d = HASH[3];
            e = HASH[4];
            f = HASH[5];
            g = HASH[6];
            h = HASH[7];
            for ( var j = 0; j<64; j++) {
                if (j < 16) W[j] = m[j + i];
                else W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);
                T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);
                T2 = safe_add(Sigma0256(a), Maj(a, b, c));
                h = g;
                g = f;
                f = e;
                e = safe_add(d, T1);
                d = c;
                c = b;
                b = a;
                a = safe_add(T1, T2);
            }
            HASH[0] = safe_add(a, HASH[0]);
            HASH[1] = safe_add(b, HASH[1]);
            HASH[2] = safe_add(c, HASH[2]);
            HASH[3] = safe_add(d, HASH[3]);
            HASH[4] = safe_add(e, HASH[4]);
            HASH[5] = safe_add(f, HASH[5]);
            HASH[6] = safe_add(g, HASH[6]);
            HASH[7] = safe_add(h, HASH[7]);
        }
        return HASH;
    }

    function str2binb (str) {
        var bin = Array();
        var mask = (1 << chrsz) - 1;
        for(var i = 0; i < str.length * chrsz; i += chrsz) {
            bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i%32);
        }
        return bin;
    }

    /**
     * @return {string}
     */
    function Utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    }

    function binb2hex (binarray) {
        var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
        var str = "";
        for(var i = 0; i < binarray.length * 4; i++) {
            str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
                hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
        }
        return str;
    }
    s = Utf8Encode(s);
    return binb2hex(core_sha256(str2binb(s), s.length * chrsz));
}