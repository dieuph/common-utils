package com.github.dieuph.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.github.dieuph.model.Role;
import com.github.dieuph.model.User;

/**
 * The Class MergeUtils.
 * 
 */
public class MergeUtils {
    /**
     * The id field we're looking for.
     * If this field is available in the classes, an equals check will be done to merge the items.
     */
    private static final String ID = "id";

    private MergeUtils() {
    }

    /**
     * Recursively merges the fields of the provider into the receiver.
     *
     * @param receiver
     *            the receiver instance.
     * @param provider
     *            the provider instance.
     */
    public static <T> void merge(final T receiver, final T provider) {
        Field[] fields = receiver.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object receiverObject = field.get(receiver);
                Object providerObject = field.get(provider);

                if (receiverObject == null || providerObject == null) {
                    System.out.println("null");
                    /* One is null */
                    field.setAccessible(true);
                    field.set(receiver, providerObject);
                } else if (field.getType().isAssignableFrom(Collection.class)) {
                    /* Collection field */
                    // noinspection rawtypes
                    System.out.println("collec");
                    mergeCollections((Collection) receiverObject, (Collection) providerObject);
                } else if (field.getType().isPrimitive() || field.getType().isEnum()
                        || field.getType().equals(String.class)) {
                    /* Primitive, Enum or String field */
                    System.out.println("primit");
                    field.setAccessible(true);
                    field.set(receiver, providerObject);
                } else { /* Mergeable field */
                    System.out.println("loop");
                    merge(receiverObject, providerObject);
                }
            } catch (IllegalAccessException e) {
                /* Should not happen */
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Recursively merges the items in the providers collection into the receivers collection.
     * Receivers not present in providers will be removed, providers not present in receivers will
     * be added.
     * If the item has a field called 'id', this field will be compared to match the items.
     *
     * @param receivers
     *            the collection containing the receiver instances.
     * @param providers
     *            the collection containing the provider instances.
     */
    public static <T> void mergeCollections(final Collection<T> receivers,
            final Collection<T> providers) {
        if (receivers == null || providers == null) {
            return;
        }

        if (receivers.isEmpty() && providers.isEmpty()) {
            return;
        }

        if (providers.isEmpty()) {
            receivers.clear();
            return;
        }

        if (receivers.isEmpty()) {
            receivers.addAll(providers);
            return;
        }

        Field idField;
        try {
            T t = providers.iterator().next();
            idField = t.getClass().getDeclaredField(ID);
            idField.setAccessible(true);
        } catch (NoSuchFieldException ignored) {
            idField = null;
        }

        try {
            if (idField != null) {
                mergeCollectionsWithId(receivers, providers, idField);
            } else {
                mergeCollectionsSimple(receivers, providers);
            }
        } catch (IllegalAccessException e) {
            /* Should not happen */
            throw new RuntimeException(e);
        }
    }

    /**
     * Recursively merges the items in the collections for which the id's are equal.
     *
     * @param receivers
     *            the collection containing the receiver items.
     * @param providers
     *            the collection containing the provider items.
     * @param idField
     *            the id field.
     *
     * @throws IllegalAccessException
     *             if the id field is not accessible.
     */
    private static <T> void mergeCollectionsWithId(final Collection<T> receivers,
            final Iterable<T> providers, final Field idField) throws IllegalAccessException {
        /* Find a receiver for each provider */
        for (T provider : providers) {
            boolean found = false;
            for (T receiver : receivers) {
                if (idField.get(receiver).equals(idField.get(provider))) {
                    merge(receiver, provider);
                    found = true;
                }
            }
            if (!found) {
                receivers.add(provider);
            }
        }

        /* Remove receivers not in providers */
        for (Iterator<T> iterator = receivers.iterator(); iterator.hasNext();) {
            T receiver = iterator.next();
            boolean found = false;
            for (T provider : providers) {
                if (idField.get(receiver).equals(idField.get(provider))) {
                    found = true;
                }
            }
            if (!found) {
                iterator.remove();
            }
        }
    }

    /**
     * Recursively merges the items in the collections one by one. Disregards equality.
     *
     * @param receivers
     *            the collection containing the receiver items.
     * @param providers
     *            the collection containing the provider items.
     */
    private static <T> void mergeCollectionsSimple(final Collection<T> receivers,
            final Iterable<T> providers) {
        Iterator<T> receiversIterator = receivers.iterator();
        Iterator<T> providersIterator = providers.iterator();
        while (receiversIterator.hasNext() && providersIterator.hasNext()) {
            merge(receiversIterator.next(), providersIterator.next());
        }

        /* Remove excessive receivers if present */
        while (receiversIterator.hasNext()) {
            receiversIterator.next();
            receiversIterator.remove();
        }

        /* Add residual providers to receivers if present */
        while (providersIterator.hasNext()) {
            receivers.add(providersIterator.next());
        }
    }

    public static void main(String[] args) throws Exception {
        User user1 = new User(1, "dieuph", Arrays.asList("manager"));
        User user2 = new User(1, "dieuph", Arrays.asList("user"));
        User user3 = new User(1, "dieuph", Arrays.asList("staff"));

        Role role1 = new Role();
        Role role2 = new Role(2, "user");
        merge(user1, user2);

        System.out.println(user1.toString());
        System.out.println(user2.toString());
    }
}
