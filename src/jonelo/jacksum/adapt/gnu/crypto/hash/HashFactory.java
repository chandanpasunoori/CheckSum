package jonelo.jacksum.adapt.gnu.crypto.hash;











































import jonelo.jacksum.adapt.gnu.crypto.Registry;
import jonelo.jacksum.adapt.gnu.crypto.hash.Haval;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HashFactory implements Registry {

   
    
    
    
    private HashFactory() {
        super();
    }

    
    
    public static IMessageDigest getInstance(String name) {
        if (name == null) {
            return null;
        }

        name = name.trim();
        IMessageDigest result = null;
        if (name.equalsIgnoreCase(WHIRLPOOL_HASH)) {
            result = new Whirlpool();
        } else if (name.equalsIgnoreCase(WHIRLPOOL2000_HASH)) {
            result = new Whirlpool2000();
        } else if (name.equalsIgnoreCase(WHIRLPOOL2003_HASH)) {
            result = new Whirlpool2003();
        } else if (name.equalsIgnoreCase(RIPEMD128_HASH)
                || name.equalsIgnoreCase(RIPEMD_128_HASH)) {
            result = new RipeMD128();
        } else if (name.equalsIgnoreCase(RIPEMD160_HASH)
                || name.equalsIgnoreCase(RIPEMD_160_HASH)) {
            result = new RipeMD160();
        } else if (name.equalsIgnoreCase(SHA160_HASH)
                || name.equalsIgnoreCase(SHA_1_HASH)
                || name.equalsIgnoreCase(SHA1_HASH)
                || name.equalsIgnoreCase(SHA_HASH)) {
            result = new Sha160();
            
        } else if (name.equalsIgnoreCase(SHA224_HASH)) {
            result = new Sha224();
        } else if (name.equalsIgnoreCase(SHA384_HASH)) {
            result = new Sha384();
        } else if (name.equalsIgnoreCase(SHA256_HASH)) {
            result = new Sha256();
        } else if (name.equalsIgnoreCase(SHA512_HASH)) {
            result = new Sha512();
        } else if (name.equalsIgnoreCase(TIGER_HASH)) {
            result = new Tiger();
        } else if (name.equalsIgnoreCase(TIGER2_HASH)) {
            result = new Tiger2();
        } else if (name.equalsIgnoreCase(TIGER160_HASH)) {
            result = new Tiger160();
        } else if (name.equalsIgnoreCase(TIGER128_HASH)) {
            result = new Tiger128();
        } else if (name.equalsIgnoreCase(HAVAL_HASH)) {
            result = new Haval();
        } else if (name.equalsIgnoreCase(MD5_HASH)) {
            result = new MD5();
        } else if (name.equalsIgnoreCase(MD4_HASH)) {
            result = new MD4();
        } else if (name.equalsIgnoreCase(MD2_HASH)) {
            result = new MD2();
        } else if (name.equalsIgnoreCase(HAVAL_HASH)) {
            result = new Haval();

            
        } else if (name.equalsIgnoreCase(HAVAL_HASH_128_3)) {
            result = new Haval(Haval.HAVAL_128_BIT, Haval.HAVAL_3_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_128_4)) {
            result = new Haval(Haval.HAVAL_128_BIT, Haval.HAVAL_4_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_128_5)) {
            result = new Haval(Haval.HAVAL_128_BIT, Haval.HAVAL_5_ROUND);

        } else if (name.equalsIgnoreCase(HAVAL_HASH_160_3)) {
            result = new Haval(Haval.HAVAL_160_BIT, Haval.HAVAL_3_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_160_4)) {
            result = new Haval(Haval.HAVAL_160_BIT, Haval.HAVAL_4_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_160_5)) {
            result = new Haval(Haval.HAVAL_160_BIT, Haval.HAVAL_5_ROUND);

        } else if (name.equalsIgnoreCase(HAVAL_HASH_192_3)) {
            result = new Haval(Haval.HAVAL_192_BIT, Haval.HAVAL_3_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_192_4)) {
            result = new Haval(Haval.HAVAL_192_BIT, Haval.HAVAL_4_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_192_5)) {
            result = new Haval(Haval.HAVAL_192_BIT, Haval.HAVAL_5_ROUND);

        } else if (name.equalsIgnoreCase(HAVAL_HASH_224_3)) {
            result = new Haval(Haval.HAVAL_224_BIT, Haval.HAVAL_3_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_224_4)) {
            result = new Haval(Haval.HAVAL_224_BIT, Haval.HAVAL_4_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_224_5)) {
            result = new Haval(Haval.HAVAL_224_BIT, Haval.HAVAL_5_ROUND);

        } else if (name.equalsIgnoreCase(HAVAL_HASH_256_3)) {
            result = new Haval(Haval.HAVAL_256_BIT, Haval.HAVAL_3_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_256_4)) {
            result = new Haval(Haval.HAVAL_256_BIT, Haval.HAVAL_4_ROUND);
        } else if (name.equalsIgnoreCase(HAVAL_HASH_256_5)) {
            result = new Haval(Haval.HAVAL_256_BIT, Haval.HAVAL_5_ROUND);
        } else if (name.equalsIgnoreCase(SHA0_HASH)) {
            result = new Sha0();
        } else if (name.equalsIgnoreCase(HAS160_HASH)) {
            result = new Has160();
        }
        






        return result;
    }

    public static final Set getNames() {
        HashSet hs = new HashSet();
        hs.add(WHIRLPOOL_HASH);
        hs.add(RIPEMD128_HASH);
        hs.add(RIPEMD160_HASH);
        hs.add(SHA160_HASH);
        
        hs.add(SHA224_HASH);
        hs.add(SHA256_HASH);
        hs.add(SHA384_HASH);
        hs.add(SHA512_HASH);
        hs.add(TIGER_HASH);
        hs.add(HAVAL_HASH);
        hs.add(MD5_HASH);
        hs.add(MD4_HASH);
        hs.add(MD2_HASH);
        hs.add(SHA0_HASH);
        return Collections.unmodifiableSet(hs);
    }

   
    
}
