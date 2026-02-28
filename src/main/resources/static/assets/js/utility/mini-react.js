/**
 * A thin render abstraction for stateful components (audio, video, timer...).
 **/
class LifecycleManager {
    // component map
    instances = new Map();

    /**
     * Use at the start of the big render function.
     * This resets the manager whenever it detects any phase change.
     **/
    beginRender() {
        if (prevState.status !== state.status) {
            // unmount elements + clear the map
            for (const [id, record] of this.instances) {
                record.instance.remove();
                this.instances.delete(id);
            }
        }
    }

    /**
     * In contrast to static UIs, which are free to destroy/recreate,
     * stateful components need conditional decisions on whether and
     * when to create, update, or destroy their (existing) UIs.
     *
     * @param id Identifier for lifecycle manager to store records internally
     * @param key Token to apply new creation or replacement whenever it changes
     * @param create Function to create the component
     * @param update (Optional) Function to sync the changes onto the component
     **/
    render(id, key, create, update) {
        const record = this.instances.get(id);

        if (!record || record.key !== key) {
            const instance = create();
            this.instances.set(id, {instance, key});
            return instance;
        }

        update?.(record.instance);
        return record.instance;
    }
}