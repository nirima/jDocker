package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * Created by magnayn on 22/07/2014.
 */
public class Identifier {
    public final Repository repository;
    public final Optional<String> tag;

    public Identifier(Repository repository, String tag) {
        this.repository = repository;

        if( tag == null )
            this.tag = Optional.absent();
        else
            this.tag = Optional.of(tag);
    }


    /**
     * Return an identifier that correctly splits up the repository and tag.
     * There can be >1 ":"
     * fred/jim     --> fred/jim, []
     * fred/jim:123 --> fred/jim, 123
     * fred:123/jim:123 --> fred:123/jim, 123
     *
     *
     * @param identifier
     * @return
     */
    public static Identifier fromCompoundString(String identifier) {
        String[] parts = identifier.split("/");
        if( parts.length != 2 ) {
            String[] rhs = identifier.split(":");
            if( rhs.length != 2 )
                return new Identifier( new Repository(identifier), null);
            else
                return new Identifier( new Repository(rhs[0]), rhs[1]);
        }

        String[] rhs = parts[1].split(":");
        if( rhs.length != 2 )
            return new Identifier( new Repository(identifier), null);

        return new Identifier( new Repository(parts[0] + "/" + rhs[0]), rhs[1]);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("repository", repository)
                .add("tag", tag)
                .toString();
    }
}
